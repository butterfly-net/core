package it.unibz.butterfly_net.core.ingestion.application;

import it.unibz.butterfly_net.core.ingestion.core.Project;
import it.unibz.butterfly_net.core.ingestion.core.ProjectRepository;
import it.unibz.butterfly_net.core.ingestion.core.errors.ProjectNotFoundError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class PostgreSQLProjectRepository implements ProjectRepository {

    private Logger logger = LoggerFactory.getLogger(PostgreSQLProjectRepository.class);
    private final Connection connection;

    public PostgreSQLProjectRepository() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Project findById(String projectId) {
        try {
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM PROJECTS WHERE id = %s", projectId);

            logger.info(query);

            ResultSet resultSet = statement.executeQuery(query);

            Project project = null;

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String authKey = resultSet.getString("auth_key");

                project = new Project(Integer.toString(id), authKey);
            }

            if (project == null) {
                ProjectNotFoundError pnfe = new ProjectNotFoundError(projectId);
                logger.error(pnfe.getMessage());
                throw pnfe;
            }

            statement.close();
            resultSet.close();
            return project;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
