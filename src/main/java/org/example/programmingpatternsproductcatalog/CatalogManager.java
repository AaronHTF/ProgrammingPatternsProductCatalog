// -------------------------------------------------------
// Assignment 3
// Written by: Aaron Ho Tim Fat 2314854
// For “Programming patterns” Section 2 – Winter 2025
// --------------------------------------------------------

package org.example.programmingpatternsproductcatalog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CatalogManager {
    private static CatalogManager catalogManager;
    private ArrayList<Product> productList;

    public CatalogManager() {
        Database db = Database.getInstance();
        db.createProductTable();
        productList = new ArrayList<>();
        loadProductsFromDatabase();
    }

    public static CatalogManager getInstance() {
        if (catalogManager == null) {
            catalogManager = new CatalogManager();
        }
        return catalogManager;
    }

    public void loadProductsFromDatabase() {
        Database db = Database.getInstance();
        ArrayList<Product> products = db.selectProducts();
        productList.addAll(products);
    }

    public void addProduct(Product product) {
        Database db = Database.getInstance();
        db.insertProduct(product);
        productList.add(product);
    }

    public void deleteProductById(int id) throws ProductNotFoundException {
        Product product = searchProduct(id);
        Database db = Database.getInstance();
        db.deleteProduct(id);
        productList.remove(product);
    }

    private Product searchProductById(int id, int first, int last) throws ProductNotFoundException {
        if (last < first) {
            throw new ProductNotFoundException();
        }
        if (productList.get(first).getId() == id) {
            return productList.get(first);
        }
        if (productList.get(last).getId() == id) {
            return productList.get(last);
        }
        return searchProductById(id, first + 1, last - 1);
    }

    public Product searchProduct(int id) throws ProductNotFoundException {
        return searchProductById(id, 0, productList.size() - 1);
    }

    public void updateProduct(int id, String name, double price, String category, int quantity) {
        try {
            Database db = Database.getInstance();
            Product product = searchProduct(id);
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            product.setCategory(category);
            product.setQuantity(quantity);
            db.updateProduct(id, name, price, category, quantity);
        }
        catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isIdTaken(int id) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public HashSet<String> getCategories() {
        HashSet<String> categories = new HashSet<>();
        for (Product product : productList) {
            categories.add(product.getCategory());
        }
        return categories;
    }

    public List<Product> filterByCategory(String category) {
        return productList.stream().filter(product -> product.getCategory().equals(category)).toList();
    }

    public List<Product> filterByPrice(double min, double max) {
        return productList.stream().filter(product -> product.getPrice() > min && product.getPrice() < max).toList();
    }

    public void sortByName() {
        productList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
    }

    public void sortByPrice() {
        productList.sort((p1, p2) -> (int) (p1.getPrice() - p2.getPrice()));
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void reloadProducts() {
        productList = new ArrayList<>();
        loadProductsFromDatabase();
    }
}
