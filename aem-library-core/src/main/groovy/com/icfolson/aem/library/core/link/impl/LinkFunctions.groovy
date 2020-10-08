package com.icfolson.aem.library.core.link.impl

import com.icfolson.aem.library.api.link.Link
import com.google.common.base.Function

final class LinkFunctions {

    public static final Function<Link, String> LINK_TO_HREF = new Function<Link, String>() {
        @Override
        String apply(Link link) {
            link.href
        }
    }

    private LinkFunctions() {

    }
}
