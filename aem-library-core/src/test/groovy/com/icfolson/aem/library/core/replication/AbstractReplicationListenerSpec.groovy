package com.icfolson.aem.library.core.replication

import com.day.cq.replication.ReplicationAction
import com.day.cq.replication.ReplicationActionType
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import org.osgi.service.event.Event
import spock.lang.Unroll

@Unroll
class AbstractReplicationListenerSpec extends AemLibrarySpec {

    private static final String PATH = "/content/icf"

    def "handle event for different replication action types"() {
        setup:
        def event = new Event(ReplicationAction.EVENT_TOPIC, [
            (ReplicationAction.PROPERTY_USER_ID): "admin",
            (ReplicationAction.PROPERTY_PATH): PATH,
            (ReplicationAction.PROPERTY_TYPE): type.name
        ])

        def listener = Mock(AbstractReplicationListener)

        when:
        listener.handleEvent(event)

        then:
        activateCount * listener.handleActivate(PATH)
        deactivateCount * listener.handleDeactivate(PATH)
        deleteCount * listener.handleDelete(PATH)

        where:
        type                             | activateCount | deactivateCount | deleteCount
        ReplicationActionType.ACTIVATE   | 1             | 0               | 0
        ReplicationActionType.DEACTIVATE | 0             | 1               | 0
        ReplicationActionType.DELETE     | 0             | 0               | 1
        ReplicationActionType.TEST       | 0             | 0               | 0
        ReplicationActionType.REVERSE    | 0             | 0               | 0
    }
}
