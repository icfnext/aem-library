package com.icfolson.aem.library.core.replication

import com.day.cq.replication.ReplicationActionType
import com.day.cq.replication.ReplicationException
import com.day.cq.replication.ReplicationStatus
import com.day.cq.replication.Replicator
import com.day.cq.wcm.api.Page
import com.day.cq.wcm.api.PageManager
import groovy.util.logging.Slf4j
import org.apache.sling.api.resource.ResourceResolverFactory
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Modified
import org.osgi.service.component.annotations.Reference
import org.osgi.service.event.EventHandler
import org.osgi.service.metatype.annotations.Designate

import javax.jcr.Session

/**
 * Replication listener that ensures ancestor pages are activated when any page receives an activation request.
 */
@Component(immediate = true, service = EventHandler, property = [
    "event.topics=com/day/cq/replication"
])
@Designate(ocd = PageReplicationListenerConfiguration)
@Slf4j("LOG")
class PageReplicationListener extends AbstractReplicationListener {

    @Reference
    protected ResourceResolverFactory resourceResolverFactory

    @Reference
    protected Replicator replicator

    private PageReplicationListenerConfiguration configuration

    @Override
    protected void handleActivate(String path) {
        if (configuration.enabled()) {
            def resourceResolver = resourceResolverFactory.getServiceResourceResolver(null)
            def session = resourceResolver.adaptTo(Session)

            def page = resourceResolver.adaptTo(PageManager).getPage(path)

            if (page) {
                def parent = page.parent

                while (parent && parent.depth > 1) {
                    def status = parent.contentResource.adaptTo(ReplicationStatus)

                    if (!status.activated) {
                        activatePage(session, parent)
                    }

                    parent = parent.parent
                }
            } else {
                LOG.debug("activated path is not a page = {}", path)
            }

            resourceResolver.close()
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

    @Activate
    @Modified
    void activate(PageReplicationListenerConfiguration configuration) {
        this.configuration = configuration
    }

    private void activatePage(Session session, Page page) {
        def path = page.path

        LOG.info("activating page = {}", path)

        try {
            replicator.replicate(session, ReplicationActionType.ACTIVATE, path)
        } catch (ReplicationException e) {
            LOG.error("error replicating page = $path", e)
        }
    }
}
