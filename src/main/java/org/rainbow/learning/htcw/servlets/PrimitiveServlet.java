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

        // We add 2 to account for the CRLF (\r\n) that will be printed in the stream after the header.
        int contentLength = line1.length() + 2;

        String header = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + contentLength + "\r\n";

        writer.println(header);
        writer.println();
        writer.println(line1);
        writer.print("Violets are blue.");
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
