package it.unibz.butterfly_net.core.ingestion.application;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import it.unibz.butterfly_net.core.ingestion.core.IngestionService;
import it.unibz.butterfly_net.core.ingestion.core.ProjectRepository;
import it.unibz.butterfly_net.core.ingestion.core.RawDataRepository;
import it.unibz.butterfly_net.core.ingestion.core.errors.CredentialsMismatchError;
import it.unibz.butterfly_net.core.ingestion.core.errors.MissingRequiredHeaderError;
import it.unibz.butterfly_net.core.ingestion.core.errors.ProjectNotFoundError;
import it.unibz.butterfly_net.core.utils.Config;
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

        ProjectRepository projectRepository = new PostgreSQLProjectRepository();
        RawDataRepository rawDataRepository = new PostgreSQLRawDataRepository();

        IngestionService ingestionService = new IngestionService(rawDataRepository, projectRepository);
        app.post("/ingest", ctx -> ingestionService
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
