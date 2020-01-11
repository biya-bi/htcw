package org.rainbow.learning.htcw.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    static {
        try {
            new LibraryLoader().loadAll();
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.startup();
        server.await();
    }

    private void await() {
        ServerSocket serverSocket = null;
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            logger.error("An I/O error occurred.", e);
            System.exit(1);
        }

        while (!shutdown) {
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                // Create and parse the request object.
                Request request = new Request(inputStream);
                request.parse();

                // Create the response object.
                Response response = new Response(outputStream);
                response.setRequest(request);

                if (request.getUri().startsWith("/servlet/")) {
                    ServletProcessor processor = new ServletProcessor();
                    processor.process(request, response);
                } else {
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }

                socket.close();

                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (IOException e) {
                logger.error("An I/O error occurred.", e);
                System.exit(1);
            }
        }
    }

    private void startup() {
        logger.info("Starting the server.");
    }
}
