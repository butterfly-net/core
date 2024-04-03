package it.unibz.butterfly_net.ingestion_api.application;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import it.unibz.butterfly_net.ingestion_api.core.IngestionService;
import it.unibz.butterfly_net.ingestion_api.core.ProjectRepository;
import it.unibz.butterfly_net.ingestion_api.core.RawDataRepository;
import it.unibz.butterfly_net.ingestion_api.core.errors.CredentialsMismatchError;
import it.unibz.butterfly_net.ingestion_api.core.errors.MissingRequiredHeaderError;
import it.unibz.butterfly_net.ingestion_api.core.errors.ProjectNotFoundError;
import it.unibz.butterfly_net.ingestion_api.core.utils.Config;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;

public class HttpServer {
    private static final Integer PORT;

    static {
        try {
            String configPort = Config.getInstance().property("SERVER_PORT");
            PORT = Integer.parseInt(configPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(Logger logger) {
        Javalin app = Javalin.create();

        app.before(ctx -> {
            logger.info(String.format("%s %s", ctx.method(), ctx.path()));
            logger.info(ctx.headerMap().toString());
            logger.info(ctx.queryParamMap().toString());
            logger.info(ctx.body());
        });

        ProjectRepository projectRepository = new InMemoryProjectRepository();
        RawDataRepository rawDataRepository = new InMemoryRawDataRepository();

        app.post("/ingest", ctx -> new IngestionService(rawDataRepository, projectRepository)
                .ingest(ctx.headerMap(), ctx.queryParamMap(), ctx.body())
        );

        app.exception(ProjectNotFoundError.class, (pnfe, ctx) -> {
            String errorMessage = pnfe.getMessage();

            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of(
                    "error", errorMessage
            ));
        });

        app.exception(MissingRequiredHeaderError.class, (mrhe, ctx) -> {
            String errorMessage = mrhe.getMessage();

            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of(
                    "error", errorMessage
            ));
        });

        app.exception(CredentialsMismatchError.class, (cme, ctx) -> {
            String errorMessage = cme.getMessage();

            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(Map.of(
                    "error", errorMessage
            ));
        });

        app.start(PORT);
    }
}
