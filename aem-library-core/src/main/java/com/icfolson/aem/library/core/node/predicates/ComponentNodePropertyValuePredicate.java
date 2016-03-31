package com.icfolson.aem.library.core.node.predicates;

import com.icfolson.aem.library.api.node.ComponentNode;
import com.google.common.base.Predicate;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ComponentNodePropertyValuePredicate<T> implements Predicate<ComponentNode> {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentNodePropertyValuePredicate.class);

    private final String propertyName;

    private final T propertyValue;

    public ComponentNodePropertyValuePredicate(final String propertyName, final T propertyValue) {
        this.propertyName = checkNotNull(propertyName);
        this.propertyValue = checkNotNull(propertyValue);
    }

    @Override
    public boolean apply(final ComponentNode componentNode) {
        final ValueMap properties = checkNotNull(componentNode).asMap();

        boolean result = false;

        if (properties.containsKey(propertyName)) {
            result = properties.get(propertyName, propertyValue.getClass()).equals(propertyValue);

            LOG.debug("property name = {}, value = {}, result = {} for component node = {}", propertyName,
                propertyValue, result, componentNode);
        } else {
            LOG.debug("property name = {}, does not exist for component node = {}", propertyName, componentNode);
        }

        return result;
    }
}
