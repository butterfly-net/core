package it.unibz.butterfly_net.core.ingestion;

import it.unibz.butterfly_net.core.ingestion.application.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Component {
    private static final Logger logger = LoggerFactory.getLogger(Component.class);

    public static void main(String[] args) {
        HttpServer.run(logger);
    }
}
