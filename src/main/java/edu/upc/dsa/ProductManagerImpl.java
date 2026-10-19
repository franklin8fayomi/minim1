package edu.upc.dsa;

import edu.upc.dsa.exceptions.EmptyOrderException;
import edu.upc.dsa.exceptions.ProductNotFoundException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.LP;
import edu.upc.dsa.models.Order;
import edu.upc.dsa.models.Product;
import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;

import java.util.*;

public class ProductManagerImpl implements ProductManager {

    private static ProductManagerImpl instance;
    private static final Logger logger = Logger.getLogger(ProductManagerImpl.class);

    // id -> producto (búsqueda O(1)); LinkedHashMap conserva el orden de inserción
    private final Map<String, Product> products;
    // nif -> usuario (búsqueda O(1))
    private final Map<String, User> users;
    // Pedidos pendientes de servir: cola FIFO (orden de llegada)
    private final Queue<Order> orderQueue;

    private ProductManagerImpl() {
        this.products = new LinkedHashMap<>();
        this.users = new HashMap<>();
        this.orderQueue = new LinkedList<>();
    }

    public static ProductManagerImpl getInstance() {
        if (instance == null) {
            instance = new ProductManagerImpl();
        }
        return instance;
    }

    @Override
    public void addUser(String id, String name, String surname, String mail) {
        logger.info("addUser(" + id + ", " + name + ", " + surname + ", " + mail + ")");
        users.put(id, new User(id, name, surname, mail));
        logger.info("addUser() -> numUsers=" + users.size());
    }

    @Override
    public void addProduct(String id, String name, double price) {
        logger.info("addProduct(" + id + ", " + name + ", " + price + ")");
        products.put(id, new Product(id, name, price));
        logger.info("addProduct() -> numProducts=" + products.size());
    }

    @Override
    public void addOrder(Order order) {
        logger.info("addOrder(" + order + ")");
        User user = users.get(order.getUserId());
        if (user == null) {
            logger.error("addOrder: user " + order.getUserId() + " does not exist");
            throw new UserNotFoundException(order.getUserId());
        }
        if (order.getLines() == null || order.getLines().isEmpty()) {
            logger.error("addOrder: order without lines");
            throw new EmptyOrderException();
        }
        for (LP lp : order.getLines()) {
            if (!products.containsKey(lp.getProductId())) {
                logger.error("addOrder: product " + lp.getProductId() + " does not exist");
                throw new ProductNotFoundException(lp.getProductId());
            }
        }
        order.setUser(user);
        orderQueue.add(order);
        logger.info("addOrder() -> pending orders=" + orderQueue.size());
    }

    @Override
    public List<Product> getProductsByPrice() {
        logger.info("getProductsByPrice()");
        List<Product> result = new ArrayList<>(products.values());
        result.sort(Comparator.comparingDouble(Product::getPrice));
        logger.info("getProductsByPrice() -> " + result);
        return result;
    }

    @Override
    public List<Product> getProductsBySales() {
        logger.info("getProductsBySales()");
        List<Product> result = new ArrayList<>(products.values());
        result.sort((p1, p2) -> Integer.compare(p2.getSales(), p1.getSales()));
        logger.info("getProductsBySales() -> " + result);
        return result;
    }

    @Override
    public Order deliverOrder() {
        logger.info("deliverOrder()");
        Order order = orderQueue.poll();
        if (order == null) {
            logger.warn("deliverOrder: there are no pending orders");
            logger.info("deliverOrder() -> null");
            return null;
        }
        for (LP lp : order.getLines()) {
            products.get(lp.getProductId()).addSales(lp.getQuantity());
        }
        order.getUser().addOrder(order);
        logger.info("deliverOrder() -> " + order);
        return order;
    }

    @Override
    public List<Order> getOrdersByUser(String userId) {
        logger.info("getOrdersByUser(" + userId + ")");
        User user = users.get(userId);
        if (user == null) {
            logger.error("getOrdersByUser: user " + userId + " does not exist");
            throw new UserNotFoundException(userId);
        }
        List<Order> result = user.orders();
        logger.info("getOrdersByUser() -> " + result);
        return result;
    }

    @Override
    public Product getProduct(String id) {
        logger.info("getProduct(" + id + ")");
        Product product = products.get(id);
        if (product == null) {
            logger.warn("getProduct: product " + id + " not found");
        }
        logger.info("getProduct() -> " + product);
        return product;
    }

    @Override
    public User getUser(String id) {
        logger.info("getUser(" + id + ")");
        User user = users.get(id);
        if (user == null) {
            logger.warn("getUser: user " + id + " not found");
        }
        logger.info("getUser() -> " + user);
        return user;
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("getAllProducts()");
        List<Product> result = new ArrayList<>(products.values());
        logger.info("getAllProducts() -> " + result.size() + " products");
        return result;
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("getAllUsers()");
        List<User> result = new ArrayList<>(users.values());
        logger.info("getAllUsers() -> " + result.size() + " users");
        return result;
    }

    @Override
    public int numOrders() {
        logger.info("numOrders()");
        int n = orderQueue.size();
        logger.info("numOrders() -> " + n);
        return n;
    }

    @Override
    public int numUsers() {
        logger.info("numUsers()");
        int n = users.size();
        logger.info("numUsers() -> " + n);
        return n;
    }

    @Override
    public int numProducts() {
        logger.info("numProducts()");
        int n = products.size();
        logger.info("numProducts() -> " + n);
        return n;
    }

    @Override
    public void clear() {
        logger.info("clear()");
        products.clear();
        users.clear();
        orderQueue.clear();
        logger.info("clear() -> done");
    }
}
