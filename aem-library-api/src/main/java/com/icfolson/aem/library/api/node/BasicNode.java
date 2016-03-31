package com.icfolson.aem.library.api.node;

import com.icfolson.aem.library.api.Accessible;
import com.icfolson.aem.library.api.ImageSource;
import com.icfolson.aem.library.api.Linkable;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.apache.sling.api.resource.Resource;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.List;

/**
 * Represents a "basic" node in the JCR, typically an unstructured node that may or may not exist in a CQ page
 * hierarchy.  Examples of non-page descendant nodes that could be considered basic nodes include design nodes and
 * arbitrary unstructured nodes that do not require inheritance capabilities.
 * <p>
 * Many methods return an <a href="https://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained#Optional">Optional</a>
 * type where a null instance would otherwise be returned (e.g. when a descendant node is requested for a path that does
 * not exist in the repository).
 */
public interface BasicNode extends Linkable, ImageSource, Accessible {

    /**
     * Get the unique ID for this resource based on the path.  If this node is the descendant of a page, the page path
     * will be removed from the identifier, since the relative path of a component node is always unique for a page.
     *
     * @return unique ID
     */
    String getId();

    /**
     * Get the index of this resource in relation to sibling nodes.
     *
     * @return index in sibling nodes or -1 if resource is null or has null parent node
     */
    int getIndex();

    /**
     * Get the index of this node in relation to sibling nodes, ignoring resource types that do not match the specified
     * value.
     *
     * @param resourceType sling:resourceType to filter on
     * @return index in sibling nodes or -1 if resource is null or has null parent node
     */
    int getIndex(String resourceType);

    /**
     * Get the JCR node for this instance.  This will return an absent <code>Optional</code> if the underlying resource
     * for this instance is synthetic or non-existent.
     *
     * @return <code>Optional</code> node for this resource
     */
    Optional<Node> getNode();

    /**
     * Shortcut for getting the current resource path.
     *
     * @return resource path
     */
    String getPath();

    /**
     * Get a list of properties that apply for the given predicate.
     *
     * @param predicate predicate to apply
     * @return filtered list of properties or empty list if no properties of this node apply for the given predicate
     * @throws RepositoryException if error occurs reading node properties
     */
    List<Property> getProperties(Predicate<Property> predicate) throws RepositoryException;

    /**
     * Get the underlying resource for this instance.
     *
     * @return current resource
     */
    Resource getResource();
}
