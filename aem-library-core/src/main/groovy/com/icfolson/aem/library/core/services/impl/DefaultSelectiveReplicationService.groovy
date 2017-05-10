package com.icfolson.aem.library.core.services.impl

import com.day.cq.replication.AgentIdFilter
import com.day.cq.replication.AgentManager
import com.day.cq.replication.ReplicationActionType
import com.day.cq.replication.ReplicationException
import com.day.cq.replication.ReplicationOptions
import com.day.cq.replication.Replicator
import com.icfolson.aem.library.core.services.SelectiveReplicationService
import groovy.util.logging.Slf4j
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference

import javax.jcr.Session

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkNotNull

@Component(service = SelectiveReplicationService)
@Slf4j("LOG")
class DefaultSelectiveReplicationService implements SelectiveReplicationService {

    @Reference
    protected Replicator replicator

    @Reference
    protected AgentManager agentManager

    @Override
    void replicate(Session session, ReplicationActionType actionType, String path, Set<String> agentIds) {
        replicate(session, actionType, path, new ReplicationOptions(), agentIds)
    }

    @Override
    void replicate(Session session, ReplicationActionType actionType, String path, ReplicationOptions options,
        Set<String> agentIds) {
        checkNotNull(options, "replication options must not be null" as Object).filter = getAgentIdFilter(agentIds)

        LOG.debug("replicating path = {} with action = {} for agent IDs = {}", path, actionType.name, agentIds)

        try {
            replicator.replicate(session, actionType, path, options)
        } catch (ReplicationException e) {
            LOG.error("error executing replication action = $actionType for path = $path", e)

            throw e
        }
    }

    private AgentIdFilter getAgentIdFilter(Set<String> agentIds) {
        checkArgument(agentManager.agents.keySet().containsAll(agentIds),
            "invalid agent IDs, one or more of the provided agent IDs does not exist = %s", agentIds)

        new AgentIdFilter(agentIds.toArray() as String[])
    }
}
