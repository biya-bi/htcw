package org.rainbow.catalina.connector.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpConnector implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HttpConnector.class);

    private boolean stopped;
    private final String scheme = "http";

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            logger.error("An I/O error occurred.", e);
            System.exit(1);
        }

        while (!stopped) {
            Socket socket = null;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                logger.error("An I/O error occurred.", e);
                continue;
            }

            HttpProcessor processor = new HttpProcessor(this);
            processor.process(socket);
        }
    }

    public void start() {
        logger.info("Starting the server.");
        Thread thread = new Thread(this);
        thread.start();
    }

    public String getScheme() {
        return scheme;
    }
}
