package edu.upc.dsa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String name;
    private String surname;
    private String mail;
    // Pedidos ya servidos de este usuario
    private List<Order> orders;

    public User() {
        this.orders = new ArrayList<>();
    }

    public User(String id, String name, String surname, String mail) {
        this();
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    // @JsonIgnore evita el ciclo User -> Order -> User al convertir a JSON
    @JsonIgnore
    public List<Order> orders() {
        return orders;
    }

    @JsonIgnore
    public int numOrders() {
        return orders.size();
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", surname=" + surname + ", mail=" + mail + "]";
    }
}
