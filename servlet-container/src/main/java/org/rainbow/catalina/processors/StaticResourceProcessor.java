package org.rainbow.catalina.processors;

import org.rainbow.catalina.servlet.Request;
import org.rainbow.catalina.servlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StaticResourceProcessor {
    private static final Logger logger = LoggerFactory.getLogger(StaticResourceProcessor.class);

    public void process(Request request, Response response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            logger.error("An expected error occurred while processing a request.", e);
        }
    }
}
