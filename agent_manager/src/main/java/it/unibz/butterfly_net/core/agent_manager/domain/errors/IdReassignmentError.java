package it.unibz.butterfly_net.core.agent_manager.domain.errors;

public class IdReassignmentError extends RuntimeException {
    public IdReassignmentError(Long newId, Long oldId, String typeName) {
        super(String.format("Attempted reassigning ID of '%s' #%d -> %d", typeName, oldId, newId));
    }
}
