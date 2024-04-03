package it.unibz.butterfly_net.ingestion_api;

import it.unibz.butterfly_net.ingestion_api.application.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Component {
    private static final Logger logger = LoggerFactory.getLogger(Component.class);

    public static void main(String[] args) {
        HttpServer.run(logger);
    }
}
