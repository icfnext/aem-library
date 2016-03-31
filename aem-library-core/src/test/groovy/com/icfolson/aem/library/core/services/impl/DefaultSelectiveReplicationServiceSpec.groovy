package com.icfolson.aem.library.core.services.impl

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import com.day.cq.replication.Agent
import com.day.cq.replication.AgentManager
import com.day.cq.replication.ReplicationActionType
import com.day.cq.replication.ReplicationOptions
import com.day.cq.replication.Replicator

class DefaultSelectiveReplicationServiceSpec extends AemLibrarySpec {

    def "valid replication request"() {
        setup:
        def service = new DefaultSelectiveReplicationService()

        def agentManager = Mock(AgentManager)
        def replicator = Mock(Replicator)

        service.agentManager = agentManager
        service.replicator = replicator

        def agent = Mock(Agent)

        when:
        service.replicate(session, ReplicationActionType.ACTIVATE, "/content", ["publish"] as Set)

        then:
        1 * agentManager.agents >> ["publish": agent]
        1 * replicator.replicate(*_)
    }

    def "valid replication request with additional options"() {
        setup:
        def service = new DefaultSelectiveReplicationService()

        def agentManager = Mock(AgentManager)
        def replicator = Mock(Replicator)

        service.agentManager = agentManager
        service.replicator = replicator

        def options = new ReplicationOptions()

        options.synchronous = true

        def agent = Mock(Agent)

        when:
        service.replicate(session, ReplicationActionType.ACTIVATE, "/content", options, ["publish"] as Set)

        then:
        1 * agentManager.agents >> ["publish": agent]
        1 * replicator.replicate(*_)
    }
}
