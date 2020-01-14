package org.rainbow.learning.htcw.client;

import java.io.*;
import java.net.Socket;

public class HttpClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        ProgramArguments programArguments = getRequest(args);

        try (Socket socket = new Socket(programArguments.getRemoteHost(), programArguments.getRemotePort());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("GET " + programArguments.getUri() + " HTTP/1.1");
            writer.println("Host: " + programArguments.getRemoteHost() + ":" + programArguments.getRemotePort());
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
                Thread.sleep(50);
            }

            System.out.println(sb.toString());
        }
    }

    private static ProgramArguments getRequest(String... args) {
        ProgramArguments programArguments = new ProgramArguments();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg == null) {
                continue;
            }
            if (i < args.length - 1) {
                String value = args[i + 1];

                switch (arg) {
                    case "--host":
                        programArguments.setRemoteHost(value);
                        break;
                    case "--port":
                        programArguments.setRemotePort(Integer.valueOf(value));
                        break;
                    case "--uri":
                        programArguments.setUri(value);
                        break;
                }
            }
        }
        return programArguments;
    }
}
