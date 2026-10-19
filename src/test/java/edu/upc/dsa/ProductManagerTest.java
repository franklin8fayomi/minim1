package edu.upc.dsa;

import edu.upc.dsa.exceptions.ProductNotFoundException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.Order;
import edu.upc.dsa.models.Product;
import edu.upc.dsa.models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ProductManagerTest {
    ProductManager pm;

    @Before
    public void setUp() {
        pm = ProductManagerImpl.getInstance();
        pm.addProduct("C1", "Coca-cola zero", 2);
        pm.addProduct("C2", "Coca-cola", 2.5);
        pm.addProduct("B1", "Lomo queso", 3);
        pm.addProduct("B2", "bacon queso", 3.5);
        pm.addProduct("D1", "Donut", 3);
        pm.addProduct("D2", "Donut xoco", 3.5);
        pm.addProduct("CF1", "Cafe", 2);
        pm.addProduct("CF2", "Tallat", 2.25);

        pm.addUser("NIF1", "El Chaval", "de la Peca", "chaval.peca@gmail.com");
        pm.addUser("NIF2", "Manolo", "Vinilo", "manolo.vinilo@gmail.com");
        pm.addUser("NIF3", "Paco", "Synth", "paco.synth@gmail.com");
        pm.addUser("NIF4", "Rafa", "Theremín", "rafa.theremin@gmail.com");
        pm.addUser("NIF5", "Lola", "Neón", "lola.neon@gmail.com");
        pm.addUser("NIF6", "Toni", "Cassette", "toni.cassette@gmail.com");
        pm.addUser("NIF7", "Juanjo", "Kazoo", "juanjo.kazoo@gmail.com");
        pm.addUser("NIF8", "Santi", "Prog", "santi.prog@gmail.com");
        pm.addUser("NIF9", "Marta", "Moog", "marta.moog@gmail.com");
        pm.addUser("NIF10", "Carlos", "Distorsión", "carlos.distorsion@gmail.com");
        pm.addUser("NIF11", "Nuria", "Vinilo", "nuria.vinilo@gmail.com");
        pm.addUser("NIF12", "Óscar", "Synthwave", "oscar.synthwave@gmail.com");
        pm.addUser("NIF13", "David", "Frikipop", "david.frikipop@gmail.com");
        pm.addUser("NIF14", "Miguel", "Guitarra", "miguel.guitarra@gmail.com");
        pm.addUser("NIF15", "Ferran", "Rockabilly", "ferran.rockabilly@gmail.com");
    }

    @After
    public void tearDown() {
        // Al ser un Singleton, hay que vaciarlo para que cada test empiece de cero
        this.pm.clear();
        this.pm = null;
    }

    @Test
    public void addProductTest() {
        Assert.assertEquals(8, pm.numProducts());
        pm.addProduct("W1", "Aigua", 1.5);
        pm.addProduct("W2", "Aigua amb gas", 2.5);
        Assert.assertEquals(10, pm.numProducts());
    }

    @Test
    public void addUserTest() {
        Assert.assertEquals(15, pm.numUsers());
        pm.addUser("NIF16", "Pepe", "Electrónico", "pepe.electronico@gmail.com");
        pm.addUser("NIF17", "Ramón", "Distorsión", "ramon.distorsion@gmail.com");
        Assert.assertEquals(17, pm.numUsers());
    }


    @Test
    public void productByPriceTest() {
        List<Product> products = pm.getProductsByPrice();
        // Orden ASCENDENTE por precio (enunciado)
        Assert.assertEquals(2.0, products.get(0).getPrice(), 0.001);
        Assert.assertEquals(2.0, products.get(1).getPrice(), 0.001);
        Assert.assertEquals(2.25, products.get(2).getPrice(), 0.001);
        Assert.assertEquals(2.5, products.get(3).getPrice(), 0.001);
        Assert.assertEquals(3.5, products.get(products.size() - 1).getPrice(), 0.001);
    }


    @Test
    public void productBySalesTest() {
        Product p1 = pm.getProduct("C1");
        Product p2 = pm.getProduct("C2");
        Product p3 = pm.getProduct("B1");
        Product p4 = pm.getProduct("B2");

        p1.setSales(20);
        p2.setSales(80);
        p3.setSales(100);
        p4.setSales(50);

        List<Product> products = pm.getProductsBySales();

        Assert.assertEquals(100, products.get(0).getSales());
        Assert.assertEquals("B1", products.get(0).getId());
        Assert.assertEquals("Lomo queso", products.get(0).getName());

        Assert.assertEquals(80, products.get(1).getSales());
        Assert.assertEquals("C2", products.get(1).getId());
        Assert.assertEquals("Coca-cola", products.get(1).getName());

        Assert.assertEquals(50, products.get(2).getSales());
        Assert.assertEquals("B2", products.get(2).getId());
        Assert.assertEquals("bacon queso", products.get(2).getName());

        Assert.assertEquals(20, products.get(3).getSales());
        Assert.assertEquals("C1", products.get(3).getId());
        Assert.assertEquals("Coca-cola zero", products.get(3).getName());

    }

    @Test
    public void addOrderTest() {
        Assert.assertEquals(0, pm.numOrders());
        Order o = new Order("NIF1");
        o.addLP(2, "C1"); //, "coca-cola");
        o.addLP(1, "B1");
        o.addLP(1, "D1");
        pm.addOrder(o);

        Assert.assertEquals(1, pm.numOrders());

        Order o2 = new Order("NIF2");
        o2.addLP(2, "C2"); //, "coca-cola");
        o2.addLP(5, "B1");
        o2.addLP(1, "CF1");
        pm.addOrder(o2);

        Assert.assertEquals(2, pm.numOrders());

        Product c1 = pm.getProduct("C1");
        Product b1 = pm.getProduct("B1");
        Product d1 = pm.getProduct("D1");
        Product cf1 = pm.getProduct("CF1");

        Assert.assertEquals(0, c1.getSales());
        Assert.assertEquals(0, b1.getSales());
        Assert.assertEquals(0, d1.getSales());
        Assert.assertEquals(0, cf1.getSales());

        User user1 =  pm.getUser("NIF1");
        Assert.assertEquals(0, user1.numOrders());

        User user2 =  pm.getUser("NIF2");
        Assert.assertEquals(0, user2.numOrders());

    }

    @Test
    public void deliverOrderTest() {

        User u1 = pm.getUser("NIF1");
        User u2 = pm.getUser("NIF2");
        Assert.assertEquals(0, u1.numOrders());
        Assert.assertEquals(0, u2.numOrders());

        Product c1 = pm.getProduct("C1");
        Product b1 = pm.getProduct("B1");
        Product d1 = pm.getProduct("D1");
        Product c2 = pm.getProduct("C2");
        Product cf1 = pm.getProduct("CF1");
        Assert.assertEquals(0, c1.getSales());
        Assert.assertEquals(0, b1.getSales());
        Assert.assertEquals(0, d1.getSales());
        Assert.assertEquals(0, c2.getSales());
        Assert.assertEquals(0, cf1.getSales());


        addOrderTest();
        Assert.assertEquals(2, pm.numOrders());
        Order o1 = pm.deliverOrder();
        Assert.assertEquals(1, pm.numOrders());
        Assert.assertEquals("NIF1", o1.getUser().getId());
        Assert.assertEquals(1, o1.getUser().numOrders());

        Assert.assertEquals(2, c1.getSales());
        Assert.assertEquals(1, b1.getSales());
        Assert.assertEquals(1, d1.getSales());
        Assert.assertEquals(0, c2.getSales());
        Assert.assertEquals(0, cf1.getSales());

        Order o2 = pm.deliverOrder();
        Assert.assertEquals(0, pm.numOrders());
        Assert.assertEquals("NIF2", o2.getUser().getId());
        Assert.assertEquals(1, o2.getUser().numOrders());

        Assert.assertEquals(2, c1.getSales());
        Assert.assertEquals(6, b1.getSales());
        Assert.assertEquals(1, d1.getSales());
        Assert.assertEquals(2, c2.getSales());
        Assert.assertEquals(1, cf1.getSales());

    }


    @Test
    public void ordersByUserTest() {
        deliverOrderTest();
        User u1 = pm.getUser("NIF1");
        List<Order> l1 = u1.orders();
        Assert.assertEquals(1, l1.size());

        User u2 = pm.getUser("NIF2");
        List<Order> l2 = u2.orders();
        Assert.assertEquals(1, l2.size());

    }

    @Test(expected = UserNotFoundException.class)
    public void addOrderUnknownUserTest() {
        Order o = new Order("NIF_NO_EXISTE");
        o.addLP(1, "C1");
        pm.addOrder(o);
    }

    @Test(expected = ProductNotFoundException.class)
    public void addOrderUnknownProductTest() {
        Order o = new Order("NIF1");
        o.addLP(1, "PRODUCTO_NO_EXISTE");
        pm.addOrder(o);
    }

    @Test
    public void deliverOrderEmptyQueueTest() {
        Assert.assertNull(pm.deliverOrder());
    }
}
