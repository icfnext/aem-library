package com.icfolson.aem.library.api.link.builders;

import com.icfolson.aem.library.api.link.ImageLink;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.link.NavigationLink;
import com.google.common.collect.SetMultimap;

import java.util.List;
import java.util.Map;

/**
 * Builder for creating <code>Link</code>, <code>ImageLink</code>, and <code>NavigationLink</code> objects.
 */
public interface LinkBuilder {

    /**
     * Build a link using the properties of the current builder.
     *
     * @return link
     */
    Link build();

    /**
     * Build an image link using the properties of the current builder.  If <code>setImage()</code> was called on the
     * builder, this is the only method that will return a link containing the image source property.
     *
     * @return image link
     */
    ImageLink buildImageLink();

    /**
     * Build a navigation link using the properties of the current builder.  If <code>setActive()</code> or
     * <code>addChild()</code> was called on the builder, this is only method that will return a link containing an
     * active state and child links.
     *
     * @return builder
     */
    NavigationLink buildNavigationLink();

    /**
     * Add a child link.  This is only applicable when building navigation links, returned by calling
     * <code>buildNavigationLink()</code>.
     *
     * @param child child navigation link instance
     * @return builder
     */
    LinkBuilder addChild(NavigationLink child);

    /**
     * Add a query parameter.
     *
     * @param name parameter name
     * @param value parameter value
     * @return builder
     */
    LinkBuilder addParameter(String name, String value);

    /**
     * Add query parameters.
     *
     * @param parameters map of parameter names to their values
     * @return builder
     */
    LinkBuilder addParameters(Map<String, String> parameters);

    /**
     * Add query parameters.
     *
     * @param parameters map of parameter names to their values
     * @return builder
     */
    LinkBuilder addParameters(SetMultimap<String, String> parameters);

    /**
     * Add properties (map of properties name-value pairs that are stored on the returned link instance).
     *
     * @param properties map of properties names to their values
     * @return builder
     */
    LinkBuilder addProperties(Map<String, String> properties);

    /**
     * Add a property (arbitrary name-value pair stored on the returned link instance).
     *
     * @param name property name
     * @param value property value
     * @return builder
     */
    LinkBuilder addProperty(String name, String value);

    /**
     * Add a selector.
     *
     * @param selector selector value
     * @return builder
     */
    LinkBuilder addSelector(String selector);

    /**
     * Add selectors.
     *
     * @param selectors list of selector values
     * @return builder
     */
    LinkBuilder addSelectors(List<String> selectors);

    /**
     * Set the active state for the link.  This only applies to navigation links returned by calling
     * <code>buildNavigationLink()</code>.
     *
     * @param isActive active state
     * @return builder
     */
    LinkBuilder setActive(boolean isActive);

    /**
     * Set the extension, without '.'.  Defaults to "html" if none is provided.
     *
     * @param extension link extension
     * @return builder
     */
    LinkBuilder setExtension(String extension);

    /**
     * Set whether the link should be considered external, i.e. not a valid content path.
     *
     * @param isExternal if true, link is marked as external
     * @return builder
     */
    LinkBuilder setExternal(boolean isExternal);

    /**
     * Set the host.  If the host is set, the href of the built link will be absolute rather than relative.
     *
     * @param host host name
     * @return builder
     */
    LinkBuilder setHost(String host);

    /**
     * Set an image source.  This only applies to image links returned by calling <code>buildImageLink()</code>.
     *
     * @param imageSource image source path
     * @return builder
     */
    LinkBuilder setImageSource(String imageSource);

    /**
     * Set the port.
     *
     * @param port port number
     * @return builder
     */
    LinkBuilder setPort(int port);

    /**
     * Set secure.  If true, the returned link will be "https" instead of "http".  This only applies when a host name is
     * set.
     *
     * @param isSecure secure
     * @return builder
     */
    LinkBuilder setSecure(boolean isSecure);

    /**
     * Set the suffix.
     *
     * @param suffix suffix
     * @return builder
     */
    LinkBuilder setSuffix(String suffix);

    /**
     * Set the link target.
     *
     * @param target link target
     * @return builder
     */
    LinkBuilder setTarget(String target);

    /**
     * Set the link title.
     *
     * @param title title
     * @return builder
     */
    LinkBuilder setTitle(String title);
}
