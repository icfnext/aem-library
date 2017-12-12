package com.icfolson.aem.library.core.node.impl

import com.icfolson.aem.library.api.link.Link
import com.icfolson.aem.library.api.link.builders.LinkBuilder
import com.icfolson.aem.library.api.node.BasicNode
import com.icfolson.aem.library.api.page.PageDecorator
import com.day.cq.commons.DownloadResource
import com.day.cq.commons.jcr.JcrConstants
import com.day.cq.dam.api.Asset
import com.day.cq.wcm.api.NameConstants
import com.day.cq.wcm.api.PageManager
import com.day.cq.wcm.foundation.Image
import com.google.common.base.Objects
import com.google.common.base.Optional
import com.google.common.base.Predicate
import com.google.common.base.Predicates
import com.google.common.collect.Iterables
import com.google.common.collect.Maps
import com.icfolson.aem.library.core.link.builders.factory.LinkBuilderFactory
import com.icfolson.aem.library.core.node.predicates.ResourcePathPredicate
import com.icfolson.aem.library.core.node.predicates.ResourceTypePredicate
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ValueMap

import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.RepositoryException
import java.lang.reflect.Array

import static com.icfolson.aem.library.core.constants.ComponentConstants.DEFAULT_IMAGE_NAME
import static com.icfolson.aem.library.core.constants.PathConstants.EXTENSION_PNG
import static com.icfolson.aem.library.core.link.impl.LinkFunctions.LINK_TO_HREF
import static com.google.common.base.Preconditions.checkNotNull

@Slf4j("LOG")
final class DefaultBasicNode extends AbstractNode implements BasicNode {

    private static final Predicate<Resource> ALL = Predicates.alwaysTrue()

    private static final String IMAGE_SELECTOR = "img"

    private final ValueMap properties

    DefaultBasicNode(Resource resource) {
        super(resource)

        properties = resource.valueMap
    }

    @Override
    boolean equals(Object other) {
        new EqualsBuilder().append(path, (other as BasicNode).path).equals
    }

    @Override
    int hashCode() {
        new HashCodeBuilder().append(path).hashCode()
    }

    @Override
    ValueMap asMap() {
        properties
    }

    @Override
    <T> T get(String propertyName, T defaultValue) {
        properties.get(checkNotNull(propertyName), defaultValue)
    }

    @Override
    <T> Optional<T> get(String propertyName, Class<T> type) {
        Optional.fromNullable(properties.get(propertyName, type))
    }

    @Override
    <AdapterType> Optional<AdapterType> getAsType(String propertyName, Class<AdapterType> type) {
        getAsTypeOptional(properties.get(checkNotNull(propertyName), ""), type)
    }

    @Override
    Optional<String> getAsHref(String propertyName) {
        getAsHref(propertyName, false)
    }

    @Override
    Optional<String> getAsHref(String propertyName, boolean strict) {
        getAsHref(propertyName, strict, false)
    }

    @Override
    Optional<String> getAsHref(String propertyName, boolean strict, boolean mapped) {
        getAsLink(propertyName, strict, mapped).transform(LINK_TO_HREF)
    }

    @Override
    Optional<Link> getAsLink(String propertyName) {
        getAsLink(propertyName, false)
    }

    @Override
    Optional<Link> getAsLink(String propertyName, boolean strict) {
        getAsLink(propertyName, strict, false)
    }

    @Override
    Optional<Link> getAsLink(String propertyName, boolean strict, boolean mapped) {
        getLinkOptional(get(propertyName, String.class), strict, mapped)
    }

    @Override
    <T> List<T> getAsList(String propertyName, Class<T> type) {
        properties.get(checkNotNull(propertyName), Array.newInstance(type, 0)) as List
    }

    @Override
    Optional<PageDecorator> getAsPage(String propertyName) {
        getPageOptional(properties.get(checkNotNull(propertyName), ""))
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
    String getId() {
        def path

        if (resource.name == JcrConstants.JCR_CONTENT) {
            path = resource.parent.path // use page path for jcr:content nodes
        } else if (resource.resourceType == NameConstants.NT_PAGE) {
            path = resource.path
        } else {
            def currentPage = resource.resourceResolver.adaptTo(PageManager).getContainingPage(resource)

            if (currentPage) {
                // remove page content path since resource path relative to jcr:content will always be unique
                path = StringUtils.removeStart(getPath(), currentPage.contentResource.path)
            } else {
                path = resource.path // non-content path
            }
        }

        path.substring(1).replaceAll("/", "-")
    }

    Optional<String> getImageReference(boolean isSelf) {
        isSelf ? Optional.fromNullable(properties.get(DownloadResource.PN_REFERENCE, String)) : imageReference
    }

    @Override
    Optional<String> getImageReference() {
        getImageReference(DEFAULT_IMAGE_NAME)
    }

    @Override
    Optional<String> getImageReference(String name) {
        Optional.fromNullable(properties.get(checkNotNull(name) + "/" + DownloadResource.PN_REFERENCE, String))
    }

    @Override
    Optional<String> getImageRendition(String renditionName) {
        getImageRendition(DEFAULT_IMAGE_NAME, checkNotNull(renditionName))
    }

    @Override
    Optional<String> getImageRendition(String name, String renditionName) {
        checkNotNull(name)
        checkNotNull(renditionName)

        def imageReferenceOptional = getImageReference(name)
        def imageRenditionOptional = Optional.absent()

        if (imageReferenceOptional.present) {
            def asset = resource.resourceResolver.getResource(imageReferenceOptional.get()).adaptTo(Asset)

            if (asset) {
                def rendition = asset.renditions.find { it.name == renditionName }

                imageRenditionOptional = Optional.fromNullable(rendition?.path)
            }
        }

        imageRenditionOptional
    }

    @Override
    Optional<String> getImageSource() {
        getImageSource(null)
    }

    @Override
    Optional<String> getImageSource(int width) {
        getImageSource(null, width)
    }

    @Override
    Optional<String> getImageSource(String name) {
        getImageSource(name, -1)
    }

    @Override
    Optional<String> getImageSource(String name, int width) {
        def optionalImageSource

        if (!name && !isHasImage(null)) {
            name = DEFAULT_IMAGE_NAME
        }

        if (isHasImage(name)) {
            def builder = new StringBuilder()

            if (JcrConstants.JCR_CONTENT == resource.name) {
                // for jcr:content nodes, use page path as start of image source
                builder.append(resource.parent.path)
            } else {
                builder.append(resource.path)
            }

            // this selector maps to the aem-library image servlet
            builder.append('.').append(IMAGE_SELECTOR)

            // only append name selector if not using the default name
            if (name && name != DEFAULT_IMAGE_NAME) {
                builder.append('.').append(name)
            }

            if (width > -1) {
                builder.append('.').append(width)
            }

            builder.append('.').append(EXTENSION_PNG)

            optionalImageSource = Optional.of(builder.toString())
        } else {
            optionalImageSource = Optional.absent()
        }

        optionalImageSource
    }

    @Override
    int getIndex() {
        getIndexForPredicate(ALL)
    }

    @Override
    int getIndex(String resourceType) {
        getIndexForPredicate(new ResourceTypePredicate(resourceType))
    }

    @Override
    Link getLink() {
        getLink(false)
    }

    @Override
    Link getLink(boolean mapped) {
        getLinkBuilder(mapped).build()
    }

    @Override
    LinkBuilder getLinkBuilder() {
        getLinkBuilder(false)
    }

    @Override
    LinkBuilder getLinkBuilder(boolean mapped) {
        LinkBuilderFactory.forResource(resource, mapped)
    }

    @Override
    Optional<Node> getNode() {
        Optional.fromNullable(resource.adaptTo(Node))
    }

    @Override
    String getPath() {
        resource.path
    }

    @Override
    List<Property> getProperties(Predicate<Property> predicate) throws RepositoryException {
        checkNotNull(predicate)

        def node = resource.adaptTo(Node)
        def properties = []

        if (node) {
            try {
                properties.addAll(node.properties.findAll { Property property -> predicate.apply(property) })
            } catch (RepositoryException e) {
                LOG.error("error getting properties for node = $path", e)
                throw e
            }
        }

        properties
    }

    @Override
    Resource getResource() {
        resourceInternal
    }

    @Override
    boolean isHasImage() {
        isHasImage(null) ?: isHasImage(DEFAULT_IMAGE_NAME)
    }

    @Override
    boolean isHasImage(String name) {
        if (name) {
            def child = resource.getChild(name)

            child && new Image(resource, name).hasContent()
        } else {
            new Image(resource).hasContent()
        }
    }

    @Override
    String toString() {
        Objects.toStringHelper(this)
            .add("path", path)
            .add("properties", Maps.newHashMap(asMap()))
            .toString()
    }

    private int getIndexForPredicate(Predicate<Resource> resourceTypePredicate) {
        def resources = Iterables.filter(resource.parent.children, resourceTypePredicate)

        Iterables.indexOf(resources, new ResourcePathPredicate(path))
    }
}
