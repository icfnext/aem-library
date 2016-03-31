package com.icfolson.aem.library.core.node.predicates;

import com.google.common.base.Predicate;
import org.apache.sling.api.resource.Resource;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ResourceTypePredicate implements Predicate<Resource> {

    /**
     * sling:resourceType property value to filter on.
     */
    private final String resourceType;

    public ResourceTypePredicate(final String resourceType) {
        this.resourceType = checkNotNull(resourceType);
    }

    @Override
    public boolean apply(final Resource resource) {
        return resourceType.equals(checkNotNull(resource).getResourceType());
    }
}
