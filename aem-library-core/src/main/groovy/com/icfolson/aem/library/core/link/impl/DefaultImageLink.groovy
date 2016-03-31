package com.icfolson.aem.library.core.link.impl

import com.icfolson.aem.library.api.link.ImageLink
import com.icfolson.aem.library.api.link.Link
import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [Link])
class DefaultImageLink implements ImageLink {

    @Delegate
    Link link

    String imageSource
}
