package it.unibz.butterfly_net.ingestion_api.application;

import it.unibz.butterfly_net.ingestion_api.core.RawDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreSQLRawDataRepository implements RawDataRepository {

    private Logger logger = LoggerFactory.getLogger(PostgreSQLRawDataRepository.class);
    private final Connection connection;

    public PostgreSQLRawDataRepository() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void store(String projectId, Long timestamp, String contentJson) {
        try {
            String insertQuery = "INSERT INTO " +
                    "RECORDS(project_id, timestamp, content)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            logger.info(preparedStatement.toString());

            preparedStatement.setInt(1, Integer.parseInt(projectId));
            preparedStatement.setLong(2, timestamp);
            preparedStatement.setString(3, contentJson);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                String log = String.format("Inserted record: #%s @ %d", projectId, timestamp);
                logger.info(log);
            }
            else {
                String log = String.format("Failed to insert record:#%s @ %d", projectId, timestamp);
                logger.warn(log);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException sqle) {
            logger.error(sqle.getMessage());
            throw new RuntimeException(sqle);
        }
    }
}
