package com.icfolson.aem.library.core.node.predicates;

import com.icfolson.aem.library.api.node.ComponentNode;
import com.google.common.base.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ComponentNodeResourceTypePredicate implements Predicate<ComponentNode> {

    /**
     * sling:resourceType property value to filter on.
     */
    private final String resourceType;

    public ComponentNodeResourceTypePredicate(final String resourceType) {
        this.resourceType = checkNotNull(resourceType);
    }

    @Override
    public boolean apply(final ComponentNode componentNode) {
        return resourceType.equals(checkNotNull(componentNode).getResource().getResourceType());
    }
}
