package com.icfolson.aem.library.core.replication

import com.icfolson.aem.library.core.services.OsgiConfiguration
import com.day.cq.replication.ReplicationAction
import com.day.cq.replication.ReplicationActionType
import com.day.cq.replication.ReplicationException
import com.day.cq.replication.ReplicationStatus
import com.day.cq.replication.Replicator
import com.day.cq.wcm.api.Page
import com.day.cq.wcm.api.PageManager
import groovy.util.logging.Slf4j
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Deactivate
import org.apache.felix.scr.annotations.Modified
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ResourceResolverFactory
import org.osgi.service.event.EventConstants
import org.osgi.service.event.EventHandler

import javax.jcr.Session

/**
 * Replication listener that ensures ancestor pages are activated when any page receives an activation request.
 */
@Component(immediate = true, metatype = true, label = "AEM Library Page Replication Listener")
@Service(EventHandler)
@Property(name = EventConstants.EVENT_TOPIC, value = ReplicationAction.EVENT_TOPIC, propertyPrivate = true)
@Slf4j("LOG")
class PageReplicationListener extends AbstractReplicationListener {

    @Property(label = "Enabled?", description = "Enable this replication listener.", boolValue = false)
    public static final String ENABLED = "enabled"

    @Reference
    ResourceResolverFactory resourceResolverFactory

    @Reference
    Replicator replicator

    private Session session

    private ResourceResolver resourceResolver

    private boolean enabled

    @Override
    protected void handleActivate(String path) {
        if (enabled) {
            def page = resourceResolver.adaptTo(PageManager).getPage(path)

            if (page) {
                def parent = page.parent

                while (parent && parent.depth > 1) {
                    def status = parent.contentResource.adaptTo(ReplicationStatus)

                    if (!status.activated) {
                        activatePage(parent)
                    }

                    parent = parent.parent
                }
            } else {
                LOG.debug "activated path is not a page = {}", path
            }
        }
    }

    @Override
    protected void handleDeactivate(String path) {
        // nothing to do
    }

    @Override
    protected void handleDelete(String path) {
        // nothing to do
    }

    @SuppressWarnings("deprecation")
    @Activate
    void activate(Map<String, Object> properties) {
        resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null)
        session = resourceResolver.adaptTo(Session)

        modified(properties)
    }

    @Modified
    void modified(Map<String, Object> properties) {
        enabled = new OsgiConfiguration(properties).getAsBoolean(ENABLED, false)
    }

    @Deactivate
    void deactivate() {
        resourceResolver?.close()
        session?.logout()
    }

    private void activatePage(Page page) {
        def path = page.path

        LOG.info "activating page = {}", path

        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE, path)
        } catch (ReplicationException e) {
            LOG.error "error replicating page = $path", e
        }
    }
}
