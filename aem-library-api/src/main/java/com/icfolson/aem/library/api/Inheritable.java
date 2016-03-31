package com.icfolson.aem.library.api;

import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.google.common.base.Optional;

import java.util.List;

/**
 * An accessible instance (such as a <code>Node</code> or <code>Page</code>) that supports hierarchy-based content
 * inheritance.
 */
public interface Inheritable {

    /**
     * Given a property on this node containing the path of another resource, get the href to the resource, using
     * inheritance if the value does not exist on this component.
     *
     * @param propertyName name of property containing a valid content path
     * @return <code>Optional</code> href
     */
    Optional<String> getAsHrefInherited(String propertyName);

    /**
     * Given a property on this node containing the path of another resource, get the href to the resource, using
     * inheritance if the value does not exist on this component.  Use this method with a <code>true</code> argument
     * when appending ".html" to the resource path is desired only for valid CQ pages and not external paths.
     *
     * @param propertyName name of property containing a valid content path
     * @param strict if true, strict resource resolution will be applied and only valid CQ content paths will have
     * ".html" appended
     * @return <code>Optional</code> href
     */
    Optional<String> getAsHrefInherited(String propertyName, boolean strict);

    /**
     * Given a property on this node containing the path of another resource, get the href to the resource, using
     * inheritance if the value does not exist on this component.  Use this method with a <code>true</code> argument
     * when appending ".html" to the resource path is desired only for valid CQ pages and not external paths.  Setting
     * <code>mapped</code> to <code>true</code> will map the path value, if it exists, through the Sling Resource
     * Resolver.
     *
     * @param propertyName name of property containing a valid content path
     * @param strict if true, strict resource resolution will be applied and only valid CQ content paths will have
     * ".html" appended
     * @param mapped if true, the property value will be routed through the Resource Resolver to determine the mapped
     * path for the value.  For example, if a mapping from "/content/" to "/" exists in the Apache Sling Resource
     * Resolver Factory OSGi configuration, getting the mapped href for the path "/content/citytechinc" will return
     * "/citytechinc.html".
     * @return <code>Optional</code> href
     */
    Optional<String> getAsHrefInherited(String propertyName, boolean strict, boolean mapped);

    /**
     * Given a property on this node containing the path of another resource, get a link to the resource, using
     * inheritance if the value does not exist on this component.
     *
     * @param propertyName name of property containing a valid content path
     * @return <code>Optional</code> link object, or null if the property does not contain a valid content path
     */
    Optional<Link> getAsLinkInherited(String propertyName);

    /**
     * Given a property on this node containing the path of another resource, get a link to the resource, using
     * inheritance if the value does not exist on this component.  Use this method with a <code>true</code> argument
     * when including an extension for the link is desired only for valid CQ pages and not external paths.
     *
     * @param propertyName name of property containing a valid content path
     * @param strict if true, strict resource resolution will be applied and only valid CQ content paths will have an
     * extension
     * @return <code>Optional</code> link object, or null if the property does not contain a valid content path
     */
    Optional<Link> getAsLinkInherited(String propertyName, boolean strict);

    /**
     * Given a property on this node containing the path of another resource, get a link to the resource, using
     * inheritance if the value does not exist on this component.  Use this method with a <code>true</code> argument
     * when including an extension for the link is desired only for valid CQ pages and not external paths.  Setting
     * <code>mapped</code> to <code>true</code> will map the path value, if it exists, through the Sling Resource
     * Resolver.
     *
     * @param propertyName name of property containing a valid content path
     * @param strict if true, strict resource resolution will be applied and only valid CQ content paths will have an
     * extension
     * @param mapped if true, the property value will be routed through the Resource Resolver to determine the mapped
     * path for the value.  For example, if a mapping from "/content/" to "/" exists in the Apache Sling Resource
     * Resolver Factory OSGi configuration, the <code>Link</code> path will be "/citytechinc" rather than
     * "/content/citytechinc".
     * @return <code>Optional</code> link object, or null if the property does not contain a valid content path
     */
    Optional<Link> getAsLinkInherited(String propertyName, boolean strict, boolean mapped);

    /**
     * Get a multi-valued property from the current node as a list of the given type, using inheritance if the value
     * does not exist on this component.
     *
     * @param propertyName name of multi-valued property
     * @param type property type
     * @param <T> property type
     * @return list of property values or an empty list if the property does not exist
     */
    <T> List<T> getAsListInherited(String propertyName, Class<T> type);

    /**
     * Get a page from the value of the given property, using inheritance if the value does not exist on this component.
     * The property value will be localized to the current page context before getting the page.
     *
     * @param propertyName property name
     * @return <code>Optional</code> page for property value
     */
    Optional<PageDecorator> getAsPageInherited(String propertyName);

    /**
     * Get an <code>Optional</code> type instance for a property on this resource containing the path of another
     * <code>Resource</code> in the repository, using inheritance if the value does not exist on this component..
     *
     * @param propertyName name of property containing a resource path
     * @param type type to adapt from resource
     * @param <AdapterType> adapter class that is adaptable from <code>Resource</code>
     * @return <code>Optional</code> instance of the specified type, or absent if either the property does not exist or
     * the resource does not adapt to the provided type
     */
    <AdapterType> Optional<AdapterType> getAsTypeInherited(String propertyName, Class<AdapterType> type);

    /**
     * @return <code>Optional</code> inherited image reference
     */
    Optional<String> getImageReferenceInherited();

    /**
     * @param name image name
     * @return <code>Optional</code> inherited image reference
     */
    Optional<String> getImageReferenceInherited(String name);

    /**
     * Get the image source for the default image (named "image") for this node. Uses the image servlet rather than a
     * direct reference to the DAM path.
     *
     * @return optional inherited image source
     */
    Optional<String> getImageSourceInherited();

    /**
     * Get the image source for the default image (named "image") for this node for the given width.
     *
     * @param width image width
     * @return optional inherited image source
     */
    Optional<String> getImageSourceInherited(int width);

    /**
     * Get the image source for this node for the named image.
     *
     * @param name image name (name of image as defined in dialog)
     * @return optional inherited image source
     */
    Optional<String> getImageSourceInherited(String name);

    /**
     * Get the image source for this node for the named image and given width.
     *
     * @param name image name (name of image as defined in dialog)
     * @param width image width
     * @return optional inherited image source
     */
    Optional<String> getImageSourceInherited(String name, int width);

    /**
     * Get a property value from the current node. If no value is found, recurse up the content tree respective to the
     * page and relative node path until a value is found.
     *
     * @param <T> result type
     * @param propertyName property to get
     * @param defaultValue value if no result is found
     * @return inherited value
     */
    <T> T getInherited(String propertyName, T defaultValue);

    /**
     * Get a property value from the current node.   If no value is found, recurse up the content tree respective to the
     * page and relative node path until a value is found, returning an absent <code>Optional</code> if not.  This
     * returns the same value as the underlying <code>ValueMap</code> wrapped in an <code>Optional</code> instance
     * instead of returning null.
     *
     * @param propertyName property name
     * @param type property type
     * @param <T> type
     * @return <code>Optional</code> of the given type containing the property value or absent if no value is found
     */
    <T> Optional<T> getInherited(String propertyName, Class<T> type);
}
