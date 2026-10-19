package edu.upc.dsa.exceptions;

public class EmptyOrderException extends RuntimeException {
    public EmptyOrderException() {
        super("The order has no lines");
    }
}
