package com.icfolson.aem.library.api;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import java.util.List;

/**
 * Definition for hierarchical JCR resources that can be traversed.
 *
 * @param <T> type of traversable resource
 */
public interface Traversable<T> {

    /**
     * Find the first ancestor resource that matches the given predicate condition.
     *
     * @param predicate predicate to match ancestor resources against
     * @return <code>Optional</code> resource that matches the predicate condition
     */
    Optional<T> findAncestor(Predicate<T> predicate);

    /**
     * Find the first ancestor resource containing the given property name.
     *
     * @param propertyName property name to find on ancestor resources
     * @return <code>Optional</code> resource that contains the property
     */
    Optional<T> findAncestorWithProperty(String propertyName);

    /**
     * Find the first ancestor resource where the given property name has the specified value.
     *
     * @param propertyName property name to find on ancestor resources
     * @param propertyValue value of named property to match
     * @param <V> type of value
     * @return <code>Optional</code> resource that contains the property value
     */
    <V> Optional<T> findAncestorWithPropertyValue(String propertyName, V propertyValue);


    /**
     * Get a list of descendant resources that match the given predicate condition.
     *
     * @param predicate predicate to match descendant resources against
     * @return list of resources that match the predicate condition or empty list if none exist
     */
    List<T> findDescendants(Predicate<T> predicate);
}
