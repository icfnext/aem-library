package com.icfolson.aem.library.core.node;

import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.link.builders.LinkBuilder;
import com.icfolson.aem.library.api.node.BasicNode;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * Extendable class that delegates to a <code>ComponentNode</code> instance.  Use when instantiating component node
 * instances directly (i.e. not in the context of a JSP or Sightly request).
 */
@JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE, isGetterVisibility = NONE)
public abstract class DelegateComponentNode implements ComponentNode {

    private final ComponentNode componentNode;

    public DelegateComponentNode(final ComponentNode componentNode) {
        this.componentNode = componentNode;
    }

    @Override
    public final ValueMap asMap() {
        return componentNode.asMap();
    }

    @Override
    public final Optional<ComponentNode> findAncestor(final Predicate<ComponentNode> predicate) {
        return componentNode.findAncestor(predicate);
    }

    @Override
    public final Optional<ComponentNode> findAncestorWithProperty(final String propertyName) {
        return componentNode.findAncestorWithProperty(propertyName);
    }

    @Override
    public final <T> Optional<ComponentNode> findAncestorWithPropertyValue(final String propertyName,
        final T propertyValue) {
        return componentNode.findAncestorWithPropertyValue(propertyName, propertyValue);
    }

    @Override
    public final List<ComponentNode> findDescendants(final Predicate<ComponentNode> predicate) {
        return componentNode.findDescendants(predicate);
    }

    @Override
    public final <T> T get(final String propertyName, final T defaultValue) {
        return componentNode.get(propertyName, defaultValue);
    }

    @Override
    public final <T> Optional<T> get(final String propertyName, final Class<T> type) {
        return componentNode.get(propertyName, type);
    }

    @Override
    public final Optional<String> getAsHref(final String propertyName) {
        return componentNode.getAsHref(propertyName);
    }

    @Override
    public final Optional<String> getAsHref(final String propertyName, final boolean strict) {
        return componentNode.getAsHref(propertyName, strict);
    }

    @Override
    public final Optional<String> getAsHref(final String propertyName, final boolean strict, final boolean mapped) {
        return componentNode.getAsHref(propertyName, strict, mapped);
    }

    @Override
    public final Optional<String> getAsHrefInherited(final String propertyName) {
        return componentNode.getAsHrefInherited(propertyName);
    }

    @Override
    public final Optional<String> getAsHrefInherited(final String propertyName, final boolean strict) {
        return componentNode.getAsHrefInherited(propertyName, strict);
    }

    @Override
    public final Optional<String> getAsHrefInherited(final String propertyName, final boolean strict,
        final boolean mapped) {
        return componentNode.getAsHrefInherited(propertyName, strict, mapped);
    }

    @Override
    public final Optional<Link> getAsLink(final String propertyName) {
        return componentNode.getAsLink(propertyName);
    }

    @Override
    public final Optional<Link> getAsLink(final String propertyName, final boolean strict) {
        return componentNode.getAsLink(propertyName, strict);
    }

    @Override
    public final Optional<Link> getAsLink(final String propertyName, final boolean strict, final boolean mapped) {
        return componentNode.getAsLink(propertyName, strict, mapped);
    }

    @Override
    public final Optional<Link> getAsLinkInherited(final String propertyName) {
        return componentNode.getAsLinkInherited(propertyName);
    }

    @Override
    public final Optional<Link> getAsLinkInherited(final String propertyName, final boolean strict) {
        return componentNode.getAsLinkInherited(propertyName, strict);
    }

    @Override
    public final Optional<Link> getAsLinkInherited(final String propertyName, final boolean strict,
        final boolean mapped) {
        return componentNode.getAsLinkInherited(propertyName, strict, mapped);
    }

    @Override
    public final <T> List<T> getAsList(final String propertyName, final Class<T> type) {
        return componentNode.getAsList(propertyName, type);
    }

    @Override
    public final <T> List<T> getAsListInherited(final String propertyName, final Class<T> type) {
        return componentNode.getAsListInherited(propertyName, type);
    }

    @Override
    public final Optional<PageDecorator> getAsPage(final String propertyName) {
        return componentNode.getAsPage(propertyName);
    }

    @Override
    public final <AdapterType> Optional<AdapterType> getAsType(final String propertyName,
        final Class<AdapterType> type) {
        return componentNode.getAsType(propertyName, type);
    }

    @Override
    public final Optional<PageDecorator> getAsPageInherited(final String propertyName) {
        return componentNode.getAsPageInherited(propertyName);
    }

    @Override
    public final <AdapterType> Optional<AdapterType> getAsTypeInherited(final String propertyName,
        final Class<AdapterType> type) {
        return componentNode.getAsTypeInherited(propertyName, type);
    }

    @Override
    public final Optional<ComponentNode> getComponentNode(final String relativePath) {
        return componentNode.getComponentNode(relativePath);
    }

    @Override
    public final List<ComponentNode> getComponentNodes() {
        return componentNode.getComponentNodes();
    }

    @Override
    public final List<ComponentNode> getComponentNodes(final Predicate<ComponentNode> predicate) {
        return componentNode.getComponentNodes(predicate);
    }

    @Override
    public final List<ComponentNode> getComponentNodes(final String relativePath) {
        return componentNode.getComponentNodes(relativePath);
    }

    @Override
    public final List<ComponentNode> getComponentNodes(final String relativePath, final String resourceType) {
        return componentNode.getComponentNodes(relativePath, resourceType);
    }

    @Override
    public final List<ComponentNode> getComponentNodes(final String relativePath,
        final Predicate<ComponentNode> predicate) {
        return componentNode.getComponentNodes(relativePath, predicate);
    }

    @Override
    public final Optional<BasicNode> getDesignNode() {
        return componentNode.getDesignNode();
    }

    @Override
    public final String getHref() {
        return componentNode.getHref();
    }

    @Override
    public final String getHref(final boolean mapped) {
        return componentNode.getHref(mapped);
    }

    @Override
    public final String getId() {
        return componentNode.getId();
    }

    @Override
    public final Optional<String> getImageReference() {
        return componentNode.getImageReference();
    }

    @Override
    public final Optional<String> getImageReference(final String name) {
        return componentNode.getImageReference(name);
    }

    @Override
    public final Optional<String> getImageReferenceInherited() {
        return componentNode.getImageReferenceInherited();
    }

    @Override
    public final Optional<String> getImageReferenceInherited(final String name) {
        return componentNode.getImageReferenceInherited(name);
    }

    @Override
    public final Optional<String> getImageRendition(final String renditionName) {
        return componentNode.getImageRendition(renditionName);
    }

    @Override
    public final Optional<String> getImageRendition(final String name, final String renditionName) {
        return componentNode.getImageRendition(name, renditionName);
    }

    @Override
    public final Optional<String> getImageSource() {
        return componentNode.getImageSource();
    }

    @Override
    public final Optional<String> getImageSource(final int width) {
        return componentNode.getImageSource(width);
    }

    @Override
    public final Optional<String> getImageSource(final String name) {
        return componentNode.getImageSource(name);
    }

    @Override
    public final Optional<String> getImageSource(final String name, final int width) {
        return componentNode.getImageSource(name, width);
    }

    @Override
    public final Optional<String> getImageSourceInherited() {
        return componentNode.getImageSourceInherited();
    }

    @Override
    public final Optional<String> getImageSourceInherited(final int width) {
        return componentNode.getImageSourceInherited(width);
    }

    @Override
    public final Optional<String> getImageSourceInherited(final String name) {
        return componentNode.getImageSourceInherited(name);
    }

    @Override
    public final Optional<String> getImageSourceInherited(final String name, final int width) {
        return componentNode.getImageSourceInherited(name, width);
    }

    @Override
    public final int getIndex() {
        return componentNode.getIndex();
    }

    @Override
    public final int getIndex(final String resourceType) {
        return componentNode.getIndex(resourceType);
    }

    @Override
    public final <T> T getInherited(final String propertyName, final T defaultValue) {
        return componentNode.getInherited(propertyName, defaultValue);
    }

    @Override
    public final <T> Optional<T> getInherited(final String propertyName, final Class<T> type) {
        return componentNode.getInherited(propertyName, type);
    }

    @Override
    public final Link getLink() {
        return componentNode.getLink();
    }

    @Override
    public final Link getLink(final boolean mapped) {
        return componentNode.getLink(mapped);
    }

    @Override
    public final LinkBuilder getLinkBuilder() {
        return componentNode.getLinkBuilder();
    }

    @Override
    public final LinkBuilder getLinkBuilder(final boolean mapped) {
        return componentNode.getLinkBuilder(mapped);
    }

    @Override
    public final Optional<Node> getNode() {
        return componentNode.getNode();
    }

    @Override
    public final Optional<BasicNode> getNodeInherited(final String relativePath) {
        return componentNode.getNodeInherited(relativePath);
    }

    @Override
    public final List<BasicNode> getNodesInherited(final String relativePath) {
        return componentNode.getNodesInherited(relativePath);
    }

    @Override
    public final Optional<ComponentNode> getParent() {
        return componentNode.getParent();
    }

    @Override
    public final String getPath() {
        return componentNode.getPath();
    }

    @Override
    public final List<Property> getProperties(final Predicate<Property> predicate) throws RepositoryException {
        return componentNode.getProperties(predicate);
    }

    @Override
    public final Resource getResource() {
        return componentNode.getResource();
    }

    @Override
    public final boolean isHasImage() {
        return componentNode.isHasImage();
    }

    @Override
    public final boolean isHasImage(final String name) {
        return componentNode.isHasImage(name);
    }
}
