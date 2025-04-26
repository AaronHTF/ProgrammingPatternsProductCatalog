package org.example.programmingpatternsproductcatalog;

public class DuplicateProductIdException extends Exception {
    public DuplicateProductIdException() {
        super("A product with this ID already exists.");
    }

    public DuplicateProductIdException(String message) {
        super(message);
    }
}
