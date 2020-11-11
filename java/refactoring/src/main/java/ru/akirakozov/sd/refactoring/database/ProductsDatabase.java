package ru.akirakozov.sd.refactoring.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductsDatabase {
    private static final String URL = "jdbc:sqlite:test.db";

    public static void createTableIfNotExists() throws SQLException {
        try (Connection c = DriverManager.getConnection(URL)) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static void dropTableIfExists() throws SQLException {
        try (Connection c = DriverManager.getConnection(URL)) {
            String sql = "DROP TABLE IF EXISTS PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public static void createNewTable() throws SQLException {
        dropTableIfExists();
        createTableIfNotExists();
    }
}
