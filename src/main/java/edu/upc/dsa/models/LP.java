package edu.upc.dsa.models;

// Línea de Pedido: un producto y la cantidad pedida
public class LP {

    private int quantity;
    private String productId;

    public LP() {
    }

    public LP(int quantity, String productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "LP [quantity=" + quantity + ", productId=" + productId + "]";
    }
}
