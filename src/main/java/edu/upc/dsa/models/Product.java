package edu.upc.dsa.models;

public class Product {

    private String id;
    private String name;
    private double price;
    private int numSales;

    public Product() {
    }

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.numSales = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSales() {
        return numSales;
    }

    public void setSales(int sales) {
        this.numSales = sales;
    }

    public void addSales(int quantity) {
        this.numSales += quantity;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", sales=" + numSales + "]";
    }
}
