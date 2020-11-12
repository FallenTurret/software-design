package ru.akirakozov.sd.refactoring.HTML;

import java.io.PrintWriter;

public class HTMLProductsWriter {
    private PrintWriter writer;

    public HTMLProductsWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void writeProduct(String name, long price) {
        writer.println(name + "\t" + price + "</br>");
    }

    public void writeHeading(String heading) {
        writer.println("<h1>" + heading + ": </h1>");
    }

    public void writeLine(String line) {
        writer.println(line);
    }

    public void beginPage() {
        writer.println("<html><body>");
    }

    public void endPage() {
        writer.println("</body></html>");
    }
}
