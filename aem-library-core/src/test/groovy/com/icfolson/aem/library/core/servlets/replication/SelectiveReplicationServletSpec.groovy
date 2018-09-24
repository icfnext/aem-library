package com.icfolson.aem.library.core.servlets.replication

import com.icfolson.aem.library.core.services.SelectiveReplicationService
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import com.day.cq.replication.AgentManager
import com.day.cq.replication.ReplicationActionType
import com.day.cq.replication.Replicator
import groovy.json.JsonBuilder
import spock.lang.Unroll

@Unroll
class SelectiveReplicationServletSpec extends AemLibrarySpec {

    def "null parameters throw exception"() {
        setup:
        def servlet = new SelectiveReplicationServlet()

        def request = requestBuilder.build()
        def response = responseBuilder.build()

        servlet.agentManager = Mock(AgentManager)
        servlet.replicator = Mock(Replicator)

        when:
        servlet.doPost(request, response)

        then:
        thrown(IllegalArgumentException)
    }

    def "invalid parameters throw exception"() {
        setup:
        def servlet = new SelectiveReplicationServlet()

        def request = requestBuilder.build {
            parameterMap = [paths: paths, agentIds: agentIds, action: action]
        }

        def response = responseBuilder.build()

        servlet.agentManager = Mock(AgentManager)
        servlet.replicator = Mock(Replicator)

        when:
        servlet.doPost(request, response)

        then:
        thrown(IllegalArgumentException)

        where:
        paths        | agentIds    | action
        []           | []          | ""
        ["/content"] | ["publish"] | ""
        []           | ["publish"] | ReplicationActionType.ACTIVATE.name()
        ["/content"] | []          | ReplicationActionType.ACTIVATE.name()
    }

    def "valid parameters"() {
        setup:
        def servlet = new SelectiveReplicationServlet()
        def paths = ["/content", "/etc"]

        def request = requestBuilder.build {
            parameterMap = [paths: paths, agentIds: ["publish"], action: ReplicationActionType.ACTIVATE.name()]
        }

        def response = responseBuilder.build()

        def replicationService = Mock(SelectiveReplicationService)

        servlet.replicationService = replicationService

        def json = new JsonBuilder(paths).toString()

        when:
        servlet.doPost(request, response)

        then:
        2 * replicationService.replicate(*_)

        then:
        response.outputAsString == json
    }
}