package com.icfolson.aem.library.core.services;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;

import javax.jcr.Session;
import java.util.Set;

/**
 * Wrapper for replicator service to facilitate replication to a selected set of agents.
 */
public interface SelectiveReplicationService {

    /**
     * Replicate content to a selected set of replication agents.
     *
     * @param session JCR session
     * @param actionType replication action
     * @param path content path to replicate
     * @param agentIds selected replication agent IDs, must be valid
     * @throws ReplicationException if error occurs during replication request
     */
    void replicate(Session session, ReplicationActionType actionType, String path, Set<String> agentIds)
        throws ReplicationException;

    /**
     * Replicate content to a selected set of replication agents with additional replication options.
     *
     * @param session JCR session
     * @param actionType replication action
     * @param path content path to replicate
     * @param options additional replication options
     * @param agentIds selected replication agent IDs, must be valid
     * @throws ReplicationException if error occurs during replication request
     */
    void replicate(Session session, ReplicationActionType actionType, String path, ReplicationOptions options,
        Set<String> agentIds) throws ReplicationException;
}
