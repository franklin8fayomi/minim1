package edu.upc.dsa.services;

import edu.upc.dsa.ProductManager;
import edu.upc.dsa.ProductManagerImpl;
import edu.upc.dsa.exceptions.EmptyOrderException;
import edu.upc.dsa.exceptions.ProductNotFoundException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.Order;
import edu.upc.dsa.models.Product;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/shop", description = "Endpoint to the campus shop service")
@Path("/shop")
public class ShopService {

    private final ProductManager pm;

    public ShopService() {
        // Jersey crea una instancia de ShopService por petición,
        // pero todas comparten el mismo ProductManager (Singleton)
        this.pm = ProductManagerImpl.getInstance();
        if (pm.numProducts() == 0) {
            pm.addProduct("C1", "Coca-cola zero", 2);
            pm.addProduct("C2", "Coca-cola", 2.5);
            pm.addProduct("B1", "Lomo queso", 3);
            pm.addProduct("B2", "bacon queso", 3.5);
            pm.addProduct("D1", "Donut", 3);
            pm.addProduct("D2", "Donut xoco", 3.5);
            pm.addProduct("CF1", "Cafe", 2);
            pm.addProduct("CF2", "Tallat", 2.25);
        }
        if (pm.numUsers() == 0) {
            pm.addUser("NIF1", "El Chaval", "de la Peca", "chaval.peca@gmail.com");
            pm.addUser("NIF2", "Manolo", "Vinilo", "manolo.vinilo@gmail.com");
            pm.addUser("NIF3", "Paco", "Synth", "paco.synth@gmail.com");
        }
    }

    // ---------- Productos ----------

    @GET
    @ApiOperation(value = "get all products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer = "List"),
    })
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        List<Product> products = pm.getAllProducts();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "add a new product")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Product.class),
            @ApiResponse(code = 400, message = "Validation Error")
    })
    @Path("/products")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(Product product) {
        if (product == null || product.getId() == null || product.getName() == null) {
            return Response.status(400).build();
        }
        pm.addProduct(product.getId(), product.getName(), product.getPrice());
        return Response.status(201).entity(pm.getProduct(product.getId())).build();
    }

    @GET
    @ApiOperation(value = "products sorted by price (ascending)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer = "List"),
    })
    @Path("/products/price")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByPrice() {
        List<Product> products = pm.getProductsByPrice();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "products sorted by number of sales (descending)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer = "List"),
    })
    @Path("/products/sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsBySales() {
        List<Product> products = pm.getProductsBySales();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }

    // ---------- Usuarios ----------

    @POST
    @ApiOperation(value = "add a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class),
            @ApiResponse(code = 400, message = "Validation Error")
    })
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        if (user == null || user.getId() == null || user.getName() == null) {
            return Response.status(400).build();
        }
        pm.addUser(user.getId(), user.getName(), user.getSurname(), user.getMail());
        return Response.status(201).entity(pm.getUser(user.getId())).build();
    }

    @GET
    @ApiOperation(value = "orders already delivered to a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Order.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/users/{id}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersByUser(@PathParam("id") String id) {
        try {
            List<Order> orders = pm.getOrdersByUser(id);
            GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(orders) {};
            return Response.status(200).entity(entity).build();
        } catch (UserNotFoundException e) {
            return Response.status(404).build();
        }
    }

    // ---------- Pedidos ----------

    @POST
    @ApiOperation(value = "place an order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Order.class),
            @ApiResponse(code = 400, message = "Empty order"),
            @ApiResponse(code = 404, message = "User or product not found")
    })
    @Path("/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrder(Order order) {
        if (order == null) {
            return Response.status(400).build();
        }
        try {
            pm.addOrder(order);
            return Response.status(201).entity(order).build();
        } catch (UserNotFoundException | ProductNotFoundException e) {
            return Response.status(404).build();
        } catch (EmptyOrderException e) {
            return Response.status(400).build();
        }
    }

    @PUT
    @ApiOperation(value = "deliver the oldest pending order (FIFO)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Order.class),
            @ApiResponse(code = 404, message = "No pending orders")
    })
    @Path("/orders/deliver")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deliverOrder() {
        Order order = pm.deliverOrder();
        if (order == null) {
            return Response.status(404).build();
        }
        return Response.status(200).entity(order).build();
    }
}
