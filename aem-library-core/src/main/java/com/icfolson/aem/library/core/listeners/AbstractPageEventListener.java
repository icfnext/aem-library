package com.icfolson.aem.library.core.listeners;

import com.icfolson.aem.library.core.utils.PathUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Base listener for <code>Page</code> events.  Implementing classes can register a listener instance using the {@link
 * javax.jcr.observation.ObservationManager} API.
 */
public abstract class AbstractPageEventListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPageEventListener.class);

    private final Session session;

    public AbstractPageEventListener(final Session session) {
        this.session = session;
    }

    @Override
    public final void onEvent(final EventIterator events) {
        final Set<String> pagePaths = new HashSet<String>();

        while (events.hasNext()) {
            final Event event = events.nextEvent();

            try {
                final String path = event.getPath();

                if (path.endsWith(JcrConstants.JCR_CONTENT)) {
                    final Node content = session.getNode(path);

                    if (content.hasProperty(NameConstants.NN_TEMPLATE)) {
                        final String pagePath = PathUtils.getPagePath(path);

                        pagePaths.add(pagePath);
                    }
                }
            } catch (RepositoryException re) {
                LOG.error("error getting event path", re);
            }
        }

        for (final String pagePath : pagePaths) {
            LOG.debug("onEvent() processing page path = {}", pagePath);

            processPage(pagePath);
        }
    }

    /**
     * Process a <code>Page</code> event.
     *
     * @param path page path
     */
    public abstract void processPage(final String path);
}
