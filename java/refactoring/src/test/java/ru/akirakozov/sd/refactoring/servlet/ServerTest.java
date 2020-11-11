package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServerTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    private void createNewTable() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE IF EXISTS PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    private void addProduct(String name, long price) throws IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("price")).thenReturn(String.valueOf(price));
        new AddProductServlet().doGet(request, response);
    }

    private void processQuery(String query) throws IOException {
        when(request.getParameter("command")).thenReturn(query);
        new QueryServlet().doGet(request, response);
    }

    void fillTheTable() throws SQLException, IOException {
        createNewTable();
        addProduct("iphone6", 300);
        addProduct("iphone7", 500);
        addProduct("iphone8", 400);
    }

    @Test
    void addShouldWork() throws SQLException, IOException {
        createNewTable();
        addProduct("iphone6", 300);
        assertEquals("OK\n", writer.toString());
    }

    @Test
    void getShouldWork() throws IOException, SQLException {
        fillTheTable();
        new GetProductsServlet().doGet(request, response);
        assertEquals("OK\n" +
                "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "iphone6\t300</br>\n" +
                "iphone7\t500</br>\n" +
                "iphone8\t400</br>\n" +
                "</body></html>\n", writer.toString());
    }

    @Test
    void maxShouldWork() throws IOException, SQLException {
        fillTheTable();
        processQuery("max");
        assertEquals("OK\n" +
                "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "iphone7\t500</br>\n" +
                "</body></html>\n", writer.toString());
    }

    @Test
    void minShouldWork() throws IOException, SQLException {
        fillTheTable();
        processQuery("min");
        assertEquals("OK\n" +
                "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "iphone6\t300</br>\n" +
                "</body></html>\n", writer.toString());
    }

    @Test
    void sumShouldWork() throws IOException, SQLException {
        fillTheTable();
        processQuery("sum");
        assertEquals("OK\n" +
                "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "Summary price: \n" +
                "1200\n" +
                "</body></html>\n", writer.toString());
    }

    @Test
    void countShouldWork() throws IOException, SQLException {
        fillTheTable();
        processQuery("count");
        assertEquals("OK\n" +
                "OK\n" +
                "OK\n" +
                "<html><body>\n" +
                "Number of products: \n" +
                "3\n" +
                "</body></html>\n", writer.toString());
    }

    @Test
    void shouldRespondCorrectlyToUnknownCommand() throws IOException, SQLException {
        fillTheTable();
        processQuery("unknown");
        assertEquals("OK\n" +
                "OK\n" +
                "OK\n" +
                "Unknown command: unknown\n", writer.toString());
    }
}