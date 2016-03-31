package com.icfolson.aem.library.core.node.predicates;

import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Property;
import javax.jcr.RepositoryException;

import static com.google.common.base.Preconditions.checkNotNull;

public final class PropertyNamePredicate implements Predicate<Property> {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyNamePredicate.class);

    private final String propertyName;

    public PropertyNamePredicate(final String propertyName) {
        this.propertyName = checkNotNull(propertyName);
    }

    @Override
    public boolean apply(final Property property) {
        checkNotNull(property);

        boolean matches = false;

        try {
            matches = propertyName.equals(property.getName());
        } catch (RepositoryException e) {
            LOG.error("error getting property name", e);
        }

        return matches;
    }
}
