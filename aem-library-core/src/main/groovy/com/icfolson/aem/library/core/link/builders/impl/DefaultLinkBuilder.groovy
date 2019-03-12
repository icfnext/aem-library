package com.icfolson.aem.library.core.link.builders.impl

import com.google.common.base.Charsets
import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Multimaps
import com.google.common.collect.SetMultimap
import com.icfolson.aem.library.api.link.ImageLink
import com.icfolson.aem.library.api.link.Link
import com.icfolson.aem.library.api.link.NavigationLink
import com.icfolson.aem.library.api.link.builders.LinkBuilder
import com.icfolson.aem.library.api.link.enums.LinkTarget
import com.icfolson.aem.library.core.constants.PathConstants
import com.icfolson.aem.library.core.link.impl.DefaultImageLink
import com.icfolson.aem.library.core.link.impl.DefaultLink
import com.icfolson.aem.library.core.link.impl.DefaultNavigationLink
import com.icfolson.aem.library.core.utils.PathUtils
import groovy.util.logging.Slf4j
import org.apache.sling.api.resource.ResourceResolver

import static com.google.common.base.Preconditions.checkNotNull

@Slf4j("LOG")
final class DefaultLinkBuilder implements LinkBuilder {

    private static final String UTF_8 = Charsets.UTF_8.name()

    private SetMultimap<String, String> parameters = LinkedHashMultimap.create()

    private final String path

    private final ResourceResolver resourceResolver

    private Map<String, String> properties = [:]

    private List<String> selectors = []

    private List<NavigationLink> children = []

    // initialized with default values

    private boolean isExternal = false

    private boolean isActive = false

    private String extension = null

    private String host = null

    private String scheme = null

    private boolean opaque = false

    private String imageSource = ""

    private int port = 0

    private boolean secure = false

    private String suffix = ""

    private String target = LinkTarget.SELF.target

    private String title = ""

    DefaultLinkBuilder(String path, ResourceResolver resourceResolver) {
        this.path = path
        this.resourceResolver = resourceResolver

        isExternal = PathUtils.isExternal(path)
    }

    @Override
    LinkBuilder addChild(NavigationLink child) {
        children.add(checkNotNull(child))

        this
    }

    @Override
    LinkBuilder addParameter(String name, String value) {
        parameters.put(checkNotNull(name), checkNotNull(value))

        this
    }

    @Override
    LinkBuilder addParameters(Map<String, String> parameters) {
        this.parameters.putAll(Multimaps.forMap(checkNotNull(parameters)))

        this
    }

    @Override
    LinkBuilder addParameters(SetMultimap<String, String> parameters) {
        this.parameters.putAll(checkNotNull(parameters))

        this
    }

    @Override
    LinkBuilder addProperties(Map<String, String> properties) {
        this.properties.putAll(checkNotNull(properties))

        this
    }

    @Override
    LinkBuilder addProperty(String name, String value) {
        properties.put(checkNotNull(name), checkNotNull(value))

        this
    }

    @Override
    LinkBuilder addSelector(String selector) {
        selectors.add(checkNotNull(selector))

        this
    }

    @Override
    LinkBuilder addSelectors(List<String> selectors) {
        this.selectors.addAll(checkNotNull(selectors))

        this
    }

    @Override
    Link build() {
        def builder = new StringBuilder().append(buildHost())

        def mappable = new StringBuilder()
            .append(path)
            .append(buildSelectors())

        def extension

        if (isExternal) {
            extension = ""
        } else {
            if (path.contains(PathConstants.SELECTOR)) {
                extension = path.substring(path.indexOf(PathConstants.SELECTOR) + 1)
            } else {
                extension = this.extension == null ? PathConstants.EXTENSION_HTML : this.extension

                if (extension) {
                    mappable.append('.').append(extension)
                }
            }
        }

        if (resourceResolver) {
            builder.append(resourceResolver.map(mappable.toString()))
        } else {
            builder.append(mappable.toString())
        }

        builder.append(suffix)

        def queryString = buildQueryString()

        builder.append(queryString)

        def href = builder.toString()

        LOG.debug("building href = {}", href)

        new DefaultLink(path, extension, suffix, href, selectors, queryString, isExternal, target, title, properties)
    }

    @Override
    ImageLink buildImageLink() {
        def link = build()

        new DefaultImageLink(link, imageSource)
    }

    @Override
    NavigationLink buildNavigationLink() {
        def link = build()

        new DefaultNavigationLink(link, isActive, children)
    }

    @Override
    LinkBuilder setActive(boolean isActive) {
        this.isActive = isActive

        this
    }

    @Override
    LinkBuilder setExtension(String extension) {
        this.extension = extension

        this
    }

    @Override
    LinkBuilder setExternal(boolean isExternal) {
        this.isExternal = isExternal

        this
    }

    @Override
    LinkBuilder setHost(String host) {
        this.host = host

        this
    }

    @Override
    LinkBuilder setScheme(String scheme) {
        this.scheme = scheme

        this
    }

    @Override
    LinkBuilder setOpaque(boolean isOpaque) {
        this.opaque = isOpaque

        this
    }

    @Override
    LinkBuilder setImageSource(String imageSource) {
        this.imageSource = imageSource

        this
    }

    @Override
    LinkBuilder setPort(int port) {
        this.port = port

        this
    }

    @Override
    LinkBuilder setSecure(boolean isSecure) {
        this.secure = isSecure

        this
    }

    @Override
    LinkBuilder setSuffix(String suffix) {
        this.suffix = suffix

        this
    }

    @Override
    LinkBuilder setTarget(String target) {
        this.target = target

        this
    }

    @Override
    LinkBuilder setTitle(String title) {
        this.title = title

        this
    }

    private String buildHost() {
        def builder = new StringBuilder()

        if (!isExternal && host) {
            if (scheme) {
                builder.append(scheme)
            } else {
                builder.append(secure ? "https" : "http")
            }

            builder.append(":")

            if (!opaque) {
                builder.append("//")
            }

            builder.append(host)

            if (port > 0) {
                builder.append(':')
                builder.append(port)
            }
        }

        if (isExternal) {
            if (scheme && !path.startsWith(scheme)) {
                builder.append(scheme).append(":")

                if (!opaque) {
                    builder.append("//")
                }
            }
        }

        builder.toString()
    }

    private String buildQueryString() {
        def builder = new StringBuilder()

        if (!parameters.isEmpty()) {
            builder.append('?')

            parameters.keySet().each { name ->
                def values = parameters.get(name)

                values.each { value ->
                    try {
                        builder.append(URLEncoder.encode(name, UTF_8))
                        builder.append('=')
                        builder.append(URLEncoder.encode(value, UTF_8))
                    } catch (UnsupportedEncodingException uee) {
                        LOG.error("invalid encoding for parameter = $name=$value", uee)
                    }

                    builder.append('&')
                }
            }

            builder.deleteCharAt(builder.length() - 1)
        }

        builder.toString()
    }

    private String buildSelectors() {
        def builder = new StringBuilder()

        if (!isExternal) {
            selectors.each { selector ->
                builder.append('.')
                builder.append(selector)
            }
        }

        builder.toString()
    }
}
