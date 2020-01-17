package org.rainbow.learning.htcw.servlets;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class PrimitiveServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("Running " + this.getClass().getEnclosingMethod().getName());
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("Running the service method.");

        PrintWriter writer = servletResponse.getWriter();

        String line1 = "Hello, Roses are red.";
        String line2 = "Violets are blue.";

        // We add 2 to account for the CRLF (\r\n) that will be printed in the stream after the header.
        int contentLength = line1.length() + line2.length() + 2;

        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html");
        writer.println(String.format("Content-Length: %s", contentLength));

        writer.println();

        writer.println(line1);
        writer.print(line2);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("Destroying the " + this.getClass().getName() + " servlet.");
    }
}
