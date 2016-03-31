package com.icfolson.aem.library.api.node;

import com.icfolson.aem.library.api.Inheritable;
import com.icfolson.aem.library.api.Traversable;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import java.util.List;

/**
 * Represents a "component" node in the JCR, typically an unstructured node in the hierarchy of a CQ page.
 * <p>
 * Many methods return an <a href="https://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained#Optional">Optional</a>
 * type where a null instance would otherwise be returned.
 */
public interface ComponentNode extends BasicNode, Inheritable, Traversable<ComponentNode> {

    /**
     * Get the component node for the resource at the given path relative to the current node.
     *
     * @param relativePath relative path to component
     * @return <code>Optional</code> node for component
     */
    Optional<ComponentNode> getComponentNode(String relativePath);

    /**
     * Get a list of child nodes for the current node.
     *
     * @return list of component nodes or empty list if none exist
     */
    List<ComponentNode> getComponentNodes();

    /**
     * Get a predicate-filtered list of child nodes for the current node.
     *
     * @param predicate predicate used to filter nodes
     * @return list of component nodes that meet the predicate criteria or empty list if none exist
     */
    List<ComponentNode> getComponentNodes(Predicate<ComponentNode> predicate);

    /**
     * Get a list of child nodes for the resource at the given path relative to this node.
     *
     * @param relativePath relative path to parent of desired nodes
     * @return list of component nodes below the specified relative path or empty list if none exist
     */
    List<ComponentNode> getComponentNodes(String relativePath);

    /**
     * Get a list of child nodes for the resource at the given path relative to this node, returning only the nodes that
     * have the specified resource type.
     *
     * @param relativePath relative path to parent of desired nodes
     * @param resourceType sling:resourceType of nodes to get from parent node
     * @return list of component nodes matching the given resource type below the specified relative path or empty list
     * if none exist
     */
    List<ComponentNode> getComponentNodes(String relativePath, String resourceType);

    /**
     * Get a list of child nodes for the resource at the given path relative to this node, returning only the nodes that
     * meet the predicate criteria.
     *
     * @param relativePath relative path to parent of desired nodes
     * @param predicate predicate used to filter nodes
     * @return list of component nodes that meet the predicate criteria below the specified relative path or empty list
     * if none exist
     */
    List<ComponentNode> getComponentNodes(String relativePath, Predicate<ComponentNode> predicate);

    /**
     * Get the design node for the current component.
     *
     * @return <code>Optional</code> design node for the current style
     */
    Optional<BasicNode> getDesignNode();

    /**
     * Get a child node relative to the current node, inheriting from a parent page if it does not exist.
     *
     * @param relativePath path relative to current node
     * @return direct child node if it exists, otherwise the child node at this relative path for an ancestor page
     */
    Optional<BasicNode> getNodeInherited(String relativePath);

    /**
     * Get the children of a node relative to the current node. If node does not exist relative to current page, inherit
     * from a parent page.
     *
     * @param relativePath path relative to current node
     * @return list of nodes representing children of the addressed node or inherited from a parent page (or empty list
     * if none exist)
     */
    List<BasicNode> getNodesInherited(String relativePath);

    /**
     * Get the parent of this node.
     *
     * @return parent component node or absent optional if resource has no parent
     */
    Optional<ComponentNode> getParent();
}
