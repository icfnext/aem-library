package com.icfolson.aem.library.core.components;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.link.builders.LinkBuilder;
import com.icfolson.aem.library.api.node.BasicNode;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.PageDecorator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * Base class for AEM component classes.
 */
@JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE, isGetterVisibility = NONE)
public abstract class AbstractComponent implements ComponentNode {

    @Inject
    private ComponentNode componentNode;

    @Override
    public String getHref() {
        return componentNode.getHref();
    }

    @Override
    public Optional<String> getImageSource() {
        return componentNode.getImageSource();
    }

    @Override
    public ValueMap asMap() {
        return componentNode.asMap();
    }

    @Override
    public String getHref(final boolean mapped) {
        return componentNode.getHref(mapped);
    }

    @Override
    public Optional<String> getAsHrefInherited(final String propertyName) {
        return componentNode.getAsHrefInherited(propertyName);
    }

    @Override
    public Link getLink() {
        return componentNode.getLink();
    }

    @Override
    public Optional<String> getImageSource(final int width) {
        return componentNode.getImageSource(width);
    }

    @Override
    public <T> T get(final String propertyName, final T defaultValue) {
        return componentNode.get(propertyName, defaultValue);
    }

    @Override
    public Optional<ComponentNode> getComponentNode(final String relativePath) {
        return componentNode.getComponentNode(relativePath);
    }

    @Override
    public Link getLink(final boolean mapped) {
        return componentNode.getLink(mapped);
    }

    @Override
    public Optional<String> getImageSource(final String name) {
        return componentNode.getImageSource(name);
    }

    @Override
    public List<ComponentNode> getComponentNodes() {
        return componentNode.getComponentNodes();
    }

    @Override
    public LinkBuilder getLinkBuilder() {
        return componentNode.getLinkBuilder();
    }

    @Override
    public Optional<String> getImageSource(final String name, final int width) {
        return componentNode.getImageSource(name, width);
    }

    @Override
    public <T> Optional<T> get(final String propertyName, final Class<T> type) {
        return componentNode.get(propertyName, type);
    }

    @Override
    public String getId() {
        return componentNode.getId();
    }

    @Override
    public Optional<String> getAsHrefInherited(final String propertyName, final boolean strict) {
        return componentNode.getAsHrefInherited(propertyName, strict);
    }

    @Override
    public List<ComponentNode> getComponentNodes(final Predicate<ComponentNode> predicate) {
        return componentNode.getComponentNodes(predicate);
    }

    @Override
    public boolean isHasImage() {
        return componentNode.isHasImage();
    }

    @Override
    public LinkBuilder getLinkBuilder(final boolean mapped) {
        return componentNode.getLinkBuilder(mapped);
    }

    @Override
    public int getIndex() {
        return componentNode.getIndex();
    }

    @Override
    public boolean isHasImage(final String name) {
        return componentNode.isHasImage(name);
    }

    @Override
    public List<ComponentNode> getComponentNodes(final String relativePath) {
        return componentNode.getComponentNodes(relativePath);
    }

    @Override
    public Optional<String> getAsHref(final String propertyName) {
        return componentNode.getAsHref(propertyName);
    }

    @Override
    public int getIndex(final String resourceType) {
        return componentNode.getIndex(resourceType);
    }

    @Override
    public Optional<Node> getNode() {
        return componentNode.getNode();
    }

    @Override
    public List<ComponentNode> getComponentNodes(final String relativePath, final String resourceType) {
        return componentNode.getComponentNodes(relativePath, resourceType);
    }

    @Override
    public String getPath() {
        return componentNode.getPath();
    }

    @Override
    public Optional<String> getAsHref(final String propertyName, final boolean strict) {
        return componentNode.getAsHref(propertyName, strict);
    }

    @Override
    public Optional<String> getAsHrefInherited(final String propertyName, final boolean strict, final boolean mapped) {
        return componentNode.getAsHrefInherited(propertyName, strict, mapped);
    }

    @Override
    public List<Property> getProperties(final Predicate<Property> predicate) throws RepositoryException {
        return componentNode.getProperties(predicate);
    }

    @Override
    public List<ComponentNode> getComponentNodes(final String relativePath,
        final Predicate<ComponentNode> predicate) {
        return componentNode.getComponentNodes(relativePath, predicate);
    }

    @Override
    public Resource getResource() {
        return componentNode.getResource();
    }

    @Override
    public Optional<BasicNode> getDesignNode() {
        return componentNode.getDesignNode();
    }

    @Override
    public Optional<Link> getAsLinkInherited(
        final String propertyName) {
        return componentNode.getAsLinkInherited(propertyName);
    }

    @Override
    public Optional<BasicNode> getNodeInherited(final String relativePath) {
        return componentNode.getNodeInherited(relativePath);
    }

    @Override
    public Optional<String> getAsHref(final String propertyName, final boolean strict, final boolean mapped) {
        return componentNode.getAsHref(propertyName, strict, mapped);
    }

    @Override
    public Optional<Link> getAsLinkInherited(final String propertyName, final boolean strict) {
        return componentNode.getAsLinkInherited(propertyName, strict);
    }

    @Override
    public List<BasicNode> getNodesInherited(final String relativePath) {
        return componentNode.getNodesInherited(relativePath);
    }

    @Override
    public Optional<ComponentNode> getParent() {
        return componentNode.getParent();
    }

    @Override
    public Optional<Link> getAsLink(final String propertyName) {
        return componentNode.getAsLink(propertyName);
    }

    @Override
    public Optional<Link> getAsLink(final String propertyName, final boolean strict) {
        return componentNode.getAsLink(propertyName, strict);
    }

    @Override
    public Optional<Link> getAsLinkInherited(final String propertyName, final boolean strict, final boolean mapped) {
        return componentNode.getAsLinkInherited(propertyName, strict, mapped);
    }

    @Override
    public <T> List<T> getAsListInherited(final String propertyName, final Class<T> type) {
        return componentNode.getAsListInherited(propertyName, type);
    }

    @Override
    public Optional<Link> getAsLink(final String propertyName, final boolean strict, final boolean mapped) {
        return componentNode.getAsLink(propertyName, strict, mapped);
    }

    @Override
    public Optional<PageDecorator> getAsPageInherited(final String propertyName) {
        return componentNode.getAsPageInherited(propertyName);
    }

    @Override
    public <T> List<T> getAsList(final String propertyName, final Class<T> type) {
        return componentNode.getAsList(propertyName, type);
    }

    @Override
    public <AdapterType> Optional<AdapterType> getAsTypeInherited(final String propertyName,
        final Class<AdapterType> type) {
        return componentNode.getAsTypeInherited(propertyName, type);
    }

    @Override
    public Optional<PageDecorator> getAsPage(final String propertyName) {
        return componentNode.getAsPage(propertyName);
    }

    @Override
    public Optional<String> getImageReferenceInherited() {
        return componentNode.getImageReferenceInherited();
    }

    @Override
    public Optional<String> getImageReferenceInherited(final String name) {
        return componentNode.getImageReferenceInherited(name);
    }

    @Override
    public Optional<String> getImageSourceInherited() {
        return componentNode.getImageSourceInherited();
    }

    @Override
    public <AdapterType> Optional<AdapterType> getAsType(final String propertyName, final Class<AdapterType> type) {
        return componentNode.getAsType(propertyName, type);
    }

    @Override
    public Optional<String> getImageSourceInherited(final int width) {
        return componentNode.getImageSourceInherited(width);
    }

    @Override
    public Optional<String> getImageReference() {
        return componentNode.getImageReference();
    }

    @Override
    public Optional<String> getImageReference(final String name) {
        return componentNode.getImageReference(name);
    }

    @Override
    public Optional<String> getImageSourceInherited(final String name) {
        return componentNode.getImageSourceInherited(name);
    }

    @Override
    public Optional<String> getImageRendition(final String renditionName) {
        return componentNode.getImageRendition(renditionName);
    }

    @Override
    public Optional<String> getImageSourceInherited(final String name, final int width) {
        return componentNode.getImageSourceInherited(name, width);
    }

    @Override
    public Optional<String> getImageRendition(final String name, final String renditionName) {
        return componentNode.getImageRendition(name, renditionName);
    }

    @Override
    public <T> T getInherited(final String propertyName, final T defaultValue) {
        return componentNode.getInherited(propertyName, defaultValue);
    }

    @Override
    public <T> Optional<T> getInherited(final String propertyName, final Class<T> type) {
        return componentNode.getInherited(propertyName, type);
    }

    @Override
    public Optional<ComponentNode> findAncestor(final Predicate<ComponentNode> predicate) {
        return componentNode.findAncestor(predicate);
    }

    @Override
    public Optional<ComponentNode> findAncestor(final Predicate<ComponentNode> predicate,
        final boolean excludeCurrentResource) {
        return componentNode.findAncestor(predicate, excludeCurrentResource);
    }

    @Override
    public Optional<ComponentNode> findAncestorWithProperty(final String propertyName) {
        return componentNode.findAncestorWithProperty(propertyName);
    }

    @Override
    public Optional<ComponentNode> findAncestorWithProperty(final String propertyName,
        final boolean excludeCurrentResource) {
        return componentNode.findAncestorWithProperty(propertyName, excludeCurrentResource);
    }

    @Override
    public <V> Optional<ComponentNode> findAncestorWithPropertyValue(final String propertyName, final V propertyValue) {
        return componentNode.findAncestorWithPropertyValue(propertyName, propertyValue);
    }

    @Override
    public <V> Optional<ComponentNode> findAncestorWithPropertyValue(final String propertyName, final V propertyValue,
        final boolean excludeCurrentResource) {
        return componentNode.findAncestorWithPropertyValue(propertyName, propertyValue, excludeCurrentResource);
    }

    @Override
    public List<ComponentNode> findDescendants(final Predicate<ComponentNode> predicate) {
        return componentNode.findDescendants(predicate);
    }
}
