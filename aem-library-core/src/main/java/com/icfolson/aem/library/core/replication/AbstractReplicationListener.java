package com.icfolson.aem.library.core.replication;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listeners extending this class need to add the following SCR annotations to register the listener.
 *
 * <pre>
 * {@literal @}Component(metatype = true)
 * {@literal @}Service
 * {@literal @}Property(name = EventConstants.EVENT_TOPIC, value = ReplicationAction.EVENT_TOPIC)
 * </pre>
 */
public abstract class AbstractReplicationListener implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractReplicationListener.class);

    @Override
    public final void handleEvent(final Event event) {
        final ReplicationAction replicationAction = ReplicationAction.fromEvent(event);

        LOG.debug("handling replication action = {}", replicationAction);

        final ReplicationActionType type = replicationAction.getType();

        for (final String path : replicationAction.getPaths()) {
            handleEvent(type, path);
        }
    }

    private void handleEvent(final ReplicationActionType type, final String path) {
        if (type.equals(ReplicationActionType.ACTIVATE)) {
            LOG.debug("handling activate event for path = {}", path);

            handleActivate(path);
        } else if (type.equals(ReplicationActionType.DEACTIVATE)) {
            LOG.debug("handling deactivate event for path = {}", path);

            handleDeactivate(path);
        } else if (type.equals(ReplicationActionType.DELETE)) {
            LOG.debug("handling delete event for path = {}", path);

            handleDelete(path);
        } else {
            LOG.trace("replication action type = {} not handled for path = {}", type, path);
        }
    }

    /**
     * Handle activation event.
     *
     * @param path payload path
     */
    protected abstract void handleActivate(final String path);

    /**
     * Handle deactivation event.
     *
     * @param path payload path
     */
    protected abstract void handleDeactivate(final String path);

    /**
     * Handle delete event.
     *
     * @param path payload path
     */
    protected abstract void handleDelete(final String path);
}
