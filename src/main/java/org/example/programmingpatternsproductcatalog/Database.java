package org.example.programmingpatternsproductcatalog;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Database dbObject;

    private Database() {

    }

    public static Database getInstance() {
        if (dbObject == null) {
            dbObject = new Database();
        }

        return dbObject;
    }

    public Connection getConnection() {
        String DB_Path = "jdbc:sqlite:src/main/resources/database/data.db";
        Connection connection;
        try {
            connection = DriverManager.getConnection(DB_Path);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void createProductTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS products(
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                price REAL,
                category TEXT,
                quantity INTEGER
                )
                """;
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Table created successfully!");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertProduct(Product product) {
        String sql = "INSERT INTO products(id, name, price, category, quantity) VALUES(?,?,?,?,?)";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setString(4, product.getCategory());
            pstmt.setInt(5, product.getQuantity());
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Product> selectProducts() {
        String sql = "SELECT * FROM products";
        ArrayList<Product> products = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String category = rs.getString("category");
                int quantity = rs.getInt("quantity");
                products.add(new Product(id, name, price, category, quantity));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    public void updateProduct(int id, String name, double price, String category, int quantity) {
        String sql = "UPDATE products SET id=?, name=?, price=?, category=?, quantity=? WHERE id=?";

        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            pstmt.setString(4, category);
            pstmt.setInt(5, quantity);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
