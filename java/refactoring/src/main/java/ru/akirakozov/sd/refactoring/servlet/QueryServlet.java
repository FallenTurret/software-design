package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.ProductsDatabase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        PrintWriter writer = response.getWriter();

        if ("max".equals(command)) {
            ProductsDatabase.getMaxProduct(writer);
        } else if ("min".equals(command)) {
            ProductsDatabase.getMinProduct(writer);
        } else if ("sum".equals(command)) {
            ProductsDatabase.getSummaryPrice(writer);
        } else if ("count".equals(command)) {
            ProductsDatabase.getCount(writer);
        } else {
            writer.println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
