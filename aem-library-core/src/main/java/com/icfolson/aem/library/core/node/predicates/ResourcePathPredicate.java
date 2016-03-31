package com.icfolson.aem.library.core.node.predicates;

import com.google.common.base.Predicate;
import org.apache.sling.api.resource.Resource;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ResourcePathPredicate implements Predicate<Resource> {

    /**
     * JCR path to match against predicate input.
     */
    private final String path;

    public ResourcePathPredicate(final String path) {
        this.path = checkNotNull(path);
    }

    @Override
    public boolean apply(final Resource resource) {
        return path.equals(checkNotNull(resource).getPath());
    }
}
