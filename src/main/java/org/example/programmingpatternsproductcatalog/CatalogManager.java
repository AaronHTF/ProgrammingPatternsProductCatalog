package org.example.programmingpatternsproductcatalog;

import java.util.ArrayList;
import java.util.List;

public class CatalogManager {
    private static CatalogManager catalogManager;
    private ArrayList<Product> productList;

    public CatalogManager() {
        productList = new ArrayList<>();
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
        productList.sort((p1, p2) -> (int) (p2.getPrice() - p1.getPrice()));
    }
}
