package edu.upc.dsa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String userId;
    private List<LP> lines;
    // Se asigna en ProductManager.addOrder() cuando se comprueba que el usuario existe
    private User user;

    public Order() {
        this.lines = new ArrayList<>();
    }

    public Order(String userId) {
        this();
        this.userId = userId;
    }

    public void addLP(int quantity, String productId) {
        this.lines.add(new LP(quantity, productId));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<LP> getLines() {
        return lines;
    }

    public void setLines(List<LP> lines) {
        this.lines = lines;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonIgnore
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order [userId=" + userId + ", lines=" + lines + "]";
    }
}
