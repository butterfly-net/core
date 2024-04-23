package it.unibz.butterfly_net.core.agent_manager.domain.usecases;

import it.unibz.butterfly_net.core.agent_manager.domain.model.*;

public class ParseRawData {
    private final ProjectRepository projectRepository;
    private final ProcessedDataRepository processedDataRepository;

    public ParseRawData(ProjectRepository projectRepository, ProcessedDataRepository processedDataRepository) {
        this.projectRepository = projectRepository;
        this.processedDataRepository = processedDataRepository;
    }

    void parse(RawData input) {
        Project project = getProjectFromRawData(input);
        Parser parser = getParser(project);
        ProcessedData processedData = parser.parse(input);
        processedDataRepository.store(processedData);
    }

    private Parser getParser(Project project) {
        return Parser.ofType(project.agentType());
    }

    private Project getProjectFromRawData(RawData input) {
        return projectRepository.findById(input.projectId());
    }
}
