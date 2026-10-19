package edu.upc.dsa;

import edu.upc.dsa.models.Order;
import edu.upc.dsa.models.Product;
import edu.upc.dsa.models.User;

import java.util.List;

public interface ProductManager {

    void addUser(String id, String name, String surname, String mail);
    void addProduct(String id, String name, double price);

    // Realizar un pedido (queda en cola pendiente de servir)
    void addOrder(Order order);

    // Listado de productos ordenado ascendentemente por precio
    List<Product> getProductsByPrice();
    // Listado de productos ordenado descendentemente por número de ventas
    List<Product> getProductsBySales();

    // Servir un pedido (en orden de llegada, FIFO)
    Order deliverOrder();

    // Listado de pedidos ya servidos de un usuario
    List<Order> getOrdersByUser(String userId);

    Product getProduct(String id);
    User getUser(String id);

    List<Product> getAllProducts();
    List<User> getAllUsers();

    int numOrders();
    int numUsers();
    int numProducts();

    void clear();
}
