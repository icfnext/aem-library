package com.icfolson.aem.library.core.node.predicates;

import com.icfolson.aem.library.api.node.ComponentNode;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ComponentNodePropertyExistsPredicate implements Predicate<ComponentNode> {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentNodePropertyExistsPredicate.class);

    private final String propertyName;

    public ComponentNodePropertyExistsPredicate(final String propertyName) {
        this.propertyName = checkNotNull(propertyName);
    }

    @Override
    public boolean apply(final ComponentNode componentNode) {
        boolean result = false;

        final Optional<Node> nodeOptional = checkNotNull(componentNode).getNode();

        if (nodeOptional.isPresent()) {
            try {
                result = nodeOptional.get().hasProperty(propertyName);
            } catch (RepositoryException e) {
                LOG.error("error checking property existence for component node", e);
            }
        }

        return result;
    }
}
