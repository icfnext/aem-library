package com.icfolson.aem.library.core.link.impl

import com.icfolson.aem.library.api.link.Link
import com.icfolson.aem.library.api.link.NavigationLink
import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [Link])
class DefaultNavigationLink implements NavigationLink {

    @Delegate
    Link link

    boolean active

    List<NavigationLink> children
}
