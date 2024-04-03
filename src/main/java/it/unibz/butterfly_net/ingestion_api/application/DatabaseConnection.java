package it.unibz.butterfly_net.ingestion_api.application;

import com.zaxxer.hikari.HikariDataSource;
import it.unibz.butterfly_net.ingestion_api.core.utils.Config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    static {
        Config config = null;
        try {
            config = Config.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(config.property("DATABASE_URL"));
        dataSource.setUsername(config.property("DATABASE_USER"));
        dataSource.setPassword(config.property("DATABASE_PASS"));
        String poolSize = config.property("DATABASE_POOL_SIZE");
        dataSource.setMaximumPoolSize(Integer.parseInt(poolSize));
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}