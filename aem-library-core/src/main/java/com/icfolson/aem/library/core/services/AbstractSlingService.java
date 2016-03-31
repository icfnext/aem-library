package com.icfolson.aem.library.core.services;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Map;

/**
 * Base class for services that require an administrative <code>Session</code> and/or <code>ResourceResolver</code>.
 */
@Component(componentAbstract = true)
@SuppressWarnings("deprecation")
public abstract class AbstractSlingService {

    @Reference
    protected SlingRepository repository;

    @Reference
    protected ResourceResolverFactory resourceResolverFactory;

    protected ResourceResolver resourceResolver;

    protected Session session;

    /**
     * Activate this service. Extending classes should call <code>getAdministrativeResourceResolver()</code> and/or
     * <code>getAdministrativeSession()</code> in this method.
     *
     * @param componentContext
     * @param properties
     */
    protected abstract void activate(final ComponentContext componentContext, final Map<String, Object> properties);

    /**
     * Deactivate this service. Extending classes should call <code>closeResourceResolver()</code> and
     * <code>closeSession()</code> in this method.
     *
     * @param componentContext
     * @param properties
     */
    protected abstract void deactivate(final ComponentContext componentContext, final Map<String, Object> properties);

    /**
     * Close the administrative resource resolver. This method should be called by the <code>@Deactivate</code> method
     * of the implementing class if the <code>getAdministrativeResourceResolver()</code> method was used at any time.
     */
    protected final void closeResourceResolver() {
        if (resourceResolver != null) {
            resourceResolver.close();
        }
    }

    /**
     * Close the administrative session. This method should be called by the <code>@Deactivate</code> method of the
     * implementing class if the <code>getAdministrativeSession()</code> method was used at any time.
     */
    protected final void closeSession() {
        if (session != null) {
            session.logout();
        }
    }

    /**
     * Get an administrative resource resolver.
     *
     * @return resource resolver
     * @throws LoginException if error occurs during authentication
     */
    protected final ResourceResolver getAdministrativeResourceResolver() throws LoginException {
        if (resourceResolver == null) {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        }

        return resourceResolver;
    }

    /**
     * Get an administrative JCR session.
     *
     * @return session
     * @throws RepositoryException if error occurs during authentication
     */
    protected final Session getAdministrativeSession() throws RepositoryException {
        if (session == null) {
            session = repository.loginAdministrative(null);
        }

        return session;
    }

    /**
     * Get the OSGi configuration for the given properties.
     *
     * @param properties map of configuration names and values
     * @return configuration wrapper
     */
    protected final OsgiConfiguration getConfiguration(final Map<String, Object> properties) {
        return new OsgiConfiguration(properties);
    }
}
