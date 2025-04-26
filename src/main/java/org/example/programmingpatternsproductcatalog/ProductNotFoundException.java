package org.example.programmingpatternsproductcatalog;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {
        super("No product with this ID was found");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
