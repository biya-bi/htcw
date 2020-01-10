package org.rainbow.learning.htcw.client;

import java.io.*;
import java.net.Socket;

public class HttpClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Request request = getRequest(args);

        try (Socket socket = new Socket(request.getRemoteHost(), request.getRemotePort());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("GET " + request.getUri() + " HTTP/1.1");
            writer.println("Host: " + request.getRemoteHost() + ":" + request.getRemotePort());
            writer.println("Connection: Close");
            writer.println();

            StringBuffer sb = new StringBuffer(8096);

            while (true) {
                if (reader.ready()) {
                    int i;
                    while ((i = reader.read()) != -1) {
                        sb.append((char) i);
                    }
                    break;
                }
                Thread.currentThread().sleep(50);
            }

            System.out.println(sb.toString());
        }
    }

    private static Request getRequest(String... args) {
        Request request = new Request();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg == null) {
                continue;
            }
            if (i < args.length - 1) {
                String value = args[i + 1];

                switch (arg) {
                    case "--host":
                        request.setRemoteHost(value);
                        break;
                    case "--port":
                        request.setRemotePort(Integer.valueOf(value));
                        break;
                    case "--uri":
                        request.setUri(value);
                        break;
                }
            }
        }
        return request;
    }
}
