package com.icfolson.aem.library.core.page.impl;
/*
import java.util.List;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.day.cq.commons.Filter;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.commons.DeepResourceIterator;
import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.icfolson.aem.library.api.link.ImageLink;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.link.NavigationLink;
import com.icfolson.aem.library.api.link.builders.LinkBuilder;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.api.page.PageManagerDecorator;
import com.icfolson.aem.library.api.page.enums.TitleType;
import com.icfolson.aem.library.core.link.builders.factory.LinkBuilderFactory;
import com.icfolson.aem.library.core.node.predicates.ComponentNodePropertyExistsPredicate;
import com.icfolson.aem.library.core.node.predicates.ComponentNodePropertyValuePredicate;
import groovy.lang.Closure;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import java.util.Collections;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

public final class DefaultPageDecorator implements PageDecorator {

    private static final Filter<Page> ALL_PAGES = page -> true;

    private final Page delegate;

    private final Optional<Resource> resourceOptional;

    private final Optional<ComponentNode> componentNodeOptional;

    public DefaultPageDecorator(Page page) {
        this.delegate = page;

        resourceOptional = Optional.ofNullable(delegate.getContentResource())
                .map(resource -> resource.adaptTo(Resource.class));

        componentNodeOptional = Optional.ofNullable(page.getContentResource())
                .map(resource -> resource.adaptTo(ComponentNode.class));
    }


    @Override
    public boolean equals(Object other) {
        return new EqualsBuilder()
                .append(delegate.getPath(), ((PageDecorator) other).getPage().getPath())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(delegate.getPath())
                .hashCode();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("path", delegate.getPath())
                .add("title", delegate.getTitle())
                .toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        final AdapterType result;

        if (type == Page.class) {
            result = (AdapterType) delegate;
        } else if (type == Resource.class) {
            result = (AdapterType) this.resourceOptional.orElse(null);
        } else {
            result = delegate.adaptTo(type);
        }

        return result;
    }

    @Override
    public ValueMap asMap() {
        getInternal({ resource -> resource..asMap() }, ValueMap.EMPTY)
    }

    @Override
    public ComponentNode getComponentNode() {
        return componentNodeOptional;
    }

    @Override
    public <T> T get(String propertyName, T defaultValue) {
        return componentNodeOptional
                .map(componentNode -> componentNode.get(propertyName, defaultValue))
                .orElse(defaultValue);
    }

    @Override
    public <T> Optional<T> get(final String propertyName, final Class<T> type) {
        return getComponentNode()
                .flatMap(componentNode -> componentNode.get(propertyName, type));
    }


    @Override
    public Optional<String> getAsHref(final String propertyName) {
        return resource.flatMap(resource -> resource.getAsHref(propertyName));
    }


    @Override
    com.google.common.base.Optional<String> getAsHref(String propertyName, boolean strict) {
        getInternal({ componentNode -> componentNode.getAsHref(propertyName, strict) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getAsHref(String propertyName, boolean strict, boolean mapped) {
        getInternal({ componentNode -> componentNode.getAsHref(propertyName, strict, mapped) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<Link> getAsLink(String propertyName) {
        getInternal({ componentNode -> componentNode.getAsLink(propertyName) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<Link> getAsLink(String propertyName, boolean strict) {
        getInternal({ componentNode -> componentNode.getAsLink(propertyName, strict) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<Link> getAsLink(String propertyName, boolean strict, boolean mapped) {
        getInternal({ componentNode -> componentNode.getAsLink(propertyName, strict, mapped) }, com.google.common.base.Optional.absent())
    }

    @Override
    <T> List<T> getAsList(String propertyName, Class<T> type) {
        getInternal({ componentNode -> componentNode.getAsList(propertyName, type) }, Collections.emptyList())
    }

    @Override
    com.google.common.base.Optional<PageDecorator> getAsPage(String propertyName) {
        getInternal({ componentNode -> componentNode.getAsPage(propertyName) }, com.google.common.base.Optional.absent())
    }

    @Override
    <AdapterType> com.google.common.base.Optional<AdapterType> getAsType(String propertyName, Class<AdapterType> type) {
        getInternal({ componentNode -> componentNode.getAsType(propertyName, type) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageReference(boolean isSelf) {
        getInternal({ componentNode -> componentNode.getImageReference(isSelf) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageReference() {
        getInternal({ componentNode -> componentNode.imageReference }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageReference(String name) {
        getInternal({ componentNode -> componentNode.getImageReference(name) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageRendition(String renditionName) {
        getInternal({ componentNode -> componentNode.getImageRendition(renditionName) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageRendition(String name, String renditionName) {
        getInternal({ componentNode -> componentNode.getImageRendition(name, renditionName) }, com.google.common.base.Optional.absent())
    }

    com.google.common.base.Optional<String> getImageSource() {
        getInternal({ componentNode -> componentNode.imageSource }, com.google.common.base.Optional.absent())
    }

    com.google.common.base.Optional<String> getImageSource(int width) {
        getInternal({ componentNode -> componentNode.getImageSource(width) }, com.google.common.base.Optional.absent())
    }

    com.google.common.base.Optional<String> getImageSource(String name) {
        getInternal({ componentNode -> componentNode.getImageSource(name) }, com.google.common.base.Optional.absent())
    }

    com.google.common.base.Optional<String> getImageSource(String name, int width) {
        getInternal({ componentNode -> componentNode.getImageSource(name, width) }, com.google.common.base.Optional.absent())
    }

    @Override
    <T> T getInherited(String propertyName, T defaultValue) {
        getInternal({ componentNode -> componentNode.getInherited(propertyName, defaultValue) }, defaultValue)
    }

    @Override
    <T> com.google.common.base.Optional<T> getInherited(String propertyName, Class<T> type) {
        getInternal({ componentNode -> componentNode.getInherited(propertyName, type) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getAsHrefInherited(String propertyName) {
        getInternal({ componentNode -> componentNode.getAsHrefInherited(propertyName) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getAsHrefInherited(String propertyName, boolean strict) {
        getInternal({ componentNode -> componentNode.getAsHrefInherited(propertyName, strict) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getAsHrefInherited(String propertyName, boolean strict, boolean mapped) {
        getInternal({ componentNode -> componentNode.getAsHrefInherited(propertyName, strict, mapped) },
                com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<Link> getAsLinkInherited(String propertyName) {
        getInternal({ componentNode -> componentNode.getAsLinkInherited(propertyName) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<Link> getAsLinkInherited(String propertyName, boolean strict) {
        getInternal({ componentNode -> componentNode.getAsLinkInherited(propertyName, strict) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<Link> getAsLinkInherited(String propertyName, boolean strict, boolean mapped) {
        getInternal({ componentNode -> componentNode.getAsLinkInherited(propertyName, strict, mapped) },
                com.google.common.base.Optional.absent())
    }

    @Override
    <T> List<T> getAsListInherited(String propertyName, Class<T> type) {
        getInternal({ componentNode -> componentNode.getAsListInherited(propertyName, type) }, Collections.emptyList())
    }

    @Override
    com.google.common.base.Optional<PageDecorator> getAsPageInherited(String propertyName) {
        getInternal({ componentNode -> componentNode.getAsPageInherited(propertyName) }, com.google.common.base.Optional.absent())
    }

    @Override
    <AdapterType> com.google.common.base.Optional<AdapterType> getAsTypeInherited(String propertyName, Class<AdapterType> type) {
        getInternal({ componentNode -> componentNode.getAsTypeInherited(propertyName, type) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageReferenceInherited(boolean isSelf) {
        getInternal({ componentNode -> componentNode.getImageReferenceInherited(isSelf) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageReferenceInherited() {
        getInternal({ componentNode -> componentNode.imageReferenceInherited }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageReferenceInherited(String name) {
        getInternal({ componentNode -> componentNode.getImageReferenceInherited(name) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageSourceInherited() {
        getInternal({ componentNode -> componentNode.imageSourceInherited }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageSourceInherited(int width) {
        getInternal({ componentNode -> componentNode.getImageSourceInherited(width) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageSourceInherited(String name) {
        getInternal({ componentNode -> componentNode.getImageSourceInherited(name) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<String> getImageSourceInherited(String name, int width) {
        getInternal({ componentNode -> componentNode.getImageSourceInherited(name, width) }, com.google.common.base.Optional.absent())
    }

    @Override
    com.google.common.base.Optional<PageDecorator> findAncestor(Predicate<PageDecorator> predicate) {
        findAncestor(predicate, false)
    }

    @Override
    com.google.common.base.Optional<PageDecorator> findAncestor(Predicate<PageDecorator> predicate, boolean excludeCurrentResource) {
        PageDecorator page = excludeCurrentResource ? parent : this
        PageDecorator ancestorPage = null

        while (page) {
            if (predicate.apply(page)) {
                ancestorPage = page
                break
            } else {
                page = page.parent
            }
        }

        com.google.common.base.Optional.fromNullable(ancestorPage)
    }

    @Override
    com.google.common.base.Optional<PageDecorator> findAncestorWithProperty(String propertyName) {
        findAncestorForPredicate(new ComponentNodePropertyExistsPredicate(propertyName), false)
    }

    @Override
    com.google.common.base.Optional<PageDecorator> findAncestorWithProperty(String propertyName, boolean excludeCurrentResource) {
        findAncestorForPredicate(new ComponentNodePropertyExistsPredicate(propertyName), excludeCurrentResource)
    }

    @Override
    <V> com.google.common.base.Optional<PageDecorator> findAncestorWithPropertyValue(String propertyName, V propertyValue) {
        findAncestorForPredicate(new ComponentNodePropertyValuePredicate<V>(propertyName, propertyValue), false)
    }

    @Override
    <V> com.google.common.base.Optional<PageDecorator> findAncestorWithPropertyValue(String propertyName, V propertyValue,
                                                                                     boolean excludeCurrentResource) {
        findAncestorForPredicate(new ComponentNodePropertyValuePredicate<V>(propertyName, propertyValue),
                excludeCurrentResource)
    }

    @Override
    List<PageDecorator> findDescendants(Predicate<PageDecorator> predicate) {
        def pages = []
        def pageManager = this.pageManager

        delegate.listChildren(ALL_PAGES, true).each { child ->
                PageDecorator page = pageManager.getPage(child)

            if (predicate.apply(page)) {
                pages.add(page)
            }
        }

        pages
    }

    @Override
    List<PageDecorator> getChildren() {
        filterChildren(Predicates.alwaysTrue(), false)
    }

    @Override
    List<PageDecorator> getChildren(boolean displayableOnly) {
        displayableOnly ? filterChildren(DISPLAYABLE_ONLY, false) : filterChildren(Predicates.alwaysTrue(), false)
    }

    @Override
    List<PageDecorator> getChildren(Predicate<PageDecorator> predicate) {
        filterChildren(checkNotNull(predicate), false)
    }

    @Override
    Iterator<PageDecorator> listChildPages() {
        listChildPages(Predicates.alwaysTrue())
    }

    @Override
    Iterator<PageDecorator> listChildPages(Predicate<PageDecorator> predicate) {
        listChildPages(predicate, false)
    }

    @Override
    Iterator<PageDecorator> listChildPages(Predicate<PageDecorator> predicate, boolean deep) {
        def resource = delegate.adaptTo(Resource)
        def iterator = deep ? new DeepResourceIterator(resource) : resource.listChildren()

        new PageDecoratorIterator(iterator, predicate)
    }

    @Override
    public java.util.Optional<PageDecorator> getChild(String name) {
        Optional<PageDecorator> child = Optional.empty();

        if (delegate.hasChild(name)) {
            child = Optional.of(delegate.adaptTo(Resource.class).getChild(name).adaptTo(PageDecorator.class));
        }

        return child;
    }


    @Override
    public Iterator<VeneeredPage> listChildren() {
        return listChildren(page -> true);
    }

    @Override
    public Iterator<VeneeredPage> listChildren(final Predicate<VeneeredPage> predicate) {
        return listChildren(predicate, false);
    }

    @Override
    public Iterator<VeneeredPage> listChildren(final Predicate<VeneeredPage> predicate, final boolean deep) {
        final Resource resource = delegate.adaptTo(Resource.class);
        final Iterator<Resource> iterator = deep ? new DeepResourceIterator(resource) : resource.listChildren();

        return new VeneeredPageIterator(iterator, predicate);
    }

    @Override
    String getHref() {
        getHref(false)
    }

    @Override
    String getHref(boolean mapped) {
        getLink(mapped).href
    }

    @Override
    ImageLink getImageLink(String imageSource) {
        LinkBuilderFactory.forPage(this).setImageSource(checkNotNull(imageSource)).buildImageLink()
    }

    boolean isHasImage() {
        getInternal({ ComponentNode componentNode -> componentNode.hasImage }, false)
    }

    boolean isHasImage(String name) {
        getInternal({ componentNode -> componentNode.isHasImage(name) }, false)
    }


    @Override
    public Link getLink() {
        return getLink(false);
    }

    @Override
    public Link getLink(TitleType titleType) {
        return getLinkBuilder(titleType, false).build();
    }

    @Override
    public Link getLink(boolean mapped) {
        return getLinkBuilder(mapped).build();
    }

    @Override
    public Link getLink(TitleType titleType, boolean mapped) {
        return getLinkBuilder(titleType, mapped).build();
    }

    @Override
    public LinkBuilder getLinkBuilder() {
        return getLinkBuilder(false);
    }

    @Override
    public LinkBuilder getLinkBuilder(TitleType titleType) {
        return getLinkBuilder(titleType, false);
    }

    @Override
    public LinkBuilder getLinkBuilder(boolean mapped) {
        return LinkBuilderFactory.forPage(delegate, mapped, TitleType.TITLE);
    }

    @Override
    public LinkBuilder getLinkBuilder(TitleType titleType, boolean mapped) {
        return LinkBuilderFactory.forPage(delegate, mapped, titleType);
    }

    @Override
    public NavigationLink getNavigationLink() {
        return getNavigationLink(false, false);
    }

    @Override
    public NavigationLink getNavigationLink(boolean isActive) {
        return getNavigationLink(isActive, false);
    }

    @Override
    public NavigationLink getNavigationLink(boolean isActive, boolean mapped) {
        return LinkBuilderFactory.forPage(delegate, mapped, TitleType.NAVIGATION_TITLE)
                .setActive(isActive)
                .buildNavigationLink();
    }

    // overrides

    @Override
    public PageManager getPageManager() {
        return delegate.adaptTo(Resource.class).resourceResolver.adaptTo(PageManager)
    }

    @Override
    public String getTitle() {
        return delegate.getTitle();
    }

    @Override
    public String getTemplatePath() {
        return delegate.getProperties().get(NameConstants.NN_TEMPLATE, String.class);
    }

    @Override
    public Optional<String> getTitle(final TitleType titleType) {
        return get(titleType.getPropertyName(), String.class);
    }

    @Override
    public PageDecorator getParent() {
        return Optional.ofNullable(delegate.getParent())
                .map(parent -> parent.adaptTo(PageDecorator.class))
                .orElse(null);
    }

    @Override
    public PageDecorator getParent(final int level) {
        return Optional.ofNullable(delegate.getParent(level))
                .map(parent -> parent.adaptTo(PageDecorator.class))
                .orElse(null);
    }

    @Override
    public PageDecorator getAbsoluteParent(final int level) {
        return Optional.ofNullable(delegate.getAbsoluteParent(level))
                .map(parent -> parent.adaptTo(PageDecorator.class))
                .orElse(null);
    }

    public PageManagerDecorator getPageManagerDecorator() {
        return delegate.getContentResource().getResourceResolver().adaptTo(PageManagerDecorator.class);
    }

    @Override
    public ReplicationStatus getReplicationStatus() {
        return delegate.adaptTo(ReplicationStatus.class);
    }

    // internals

    private <T> T getInternal(Closure<T> closure, T defaultValue) {
        componentNodeOptional.present ? closure.call(componentNodeOptional.get()) : defaultValue
    }

    private Optional<PageDecorator> findAncestorForPredicate(Predicate<ComponentNode> predicate,
                                                             boolean excludeCurrentResource) {
        PageDecorator page = excludeCurrentResource ? getParent() : this;
        PageDecorator ancestorPage = null;

        while (page != null) {
            final Optional<ComponentNode> optionalComponentNode = page.getComponentNode();

            if (optionalComponentNode.isPresent() && predicate.test(optionalComponentNode.get())) {
                ancestorPage = page;
                break;
            } else {
                page = page.getParent();
            }
        }

        return Optional.ofNullable(ancestorPage);
    }

    private List<PageDecorator> filterChildren(Predicate<PageDecorator> predicate, boolean deep) {
        def pages = []
        def pageManager = this.pageManager

        delegate.listChildren(ALL_PAGES, deep).each { child ->
                def page = pageManager.getPage(child)

            if (page && predicate.apply(page)) {
                pages.add(page)
            }
        }

        pages
    }
}*/

