package ru.akirakozov.sd.refactoring.database;

import ru.akirakozov.sd.refactoring.HTML.HTMLProductsWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class ProductsDatabase {
    private static final String URL = "jdbc:sqlite:test.db";

    public static void createTableIfNotExists() throws SQLException {
        replaceTable("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    public static void dropTableIfExists() throws SQLException {
        replaceTable("DROP TABLE IF EXISTS PRODUCT");
    }

    public static void createNewTable() throws SQLException {
        dropTableIfExists();
        createTableIfNotExists();
    }

    public static void addProduct(String name, long price) {
        ProductsDatabase.updateTable("INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")");
    }

    public static void getProducts(HttpServletRequest request, HttpServletResponse response) {
        try {
            HTMLProductsWriter writer = new HTMLProductsWriter(response.getWriter());
            try (Connection c = DriverManager.getConnection(URL)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
                writer.beginPage();

                while (rs.next()) {
                    String name = rs.getString("name");
                    long price = rs.getInt("price");
                    writer.writeProduct(name, price);
                }
                writer.endPage();

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateTable(String sql) {
        try {
            try (Connection c = DriverManager.getConnection(URL)) {
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void replaceTable(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection(URL)) {
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }
}
