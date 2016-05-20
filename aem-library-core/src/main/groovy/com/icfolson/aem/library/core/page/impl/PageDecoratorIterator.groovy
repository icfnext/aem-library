package com.icfolson.aem.library.core.page.impl

import com.google.common.base.Predicate
import com.icfolson.aem.library.api.page.PageDecorator
import org.apache.sling.api.resource.Resource

class PageDecoratorIterator implements Iterator<PageDecorator> {

    private PageDecorator next

    private final Iterator<Resource> base

    private final Predicate<PageDecorator> predicate

    PageDecoratorIterator(Iterator<Resource> base, Predicate<PageDecorator> predicate) {
        this.base = base
        this.predicate = predicate

        seek()
    }

    @Override
    boolean hasNext() {
        next
    }

    @Override
    PageDecorator next() {
        if (next) {
            seek()
        } else {
            throw new NoSuchElementException()
        }
    }

    @Override
    void remove() {
        throw new UnsupportedOperationException()
    }

    private PageDecorator seek() {
        def prev = next

        next = null

        while (base.hasNext() && !next) {
            def resource = base.next()

            next = resource.adaptTo(PageDecorator)

            if (next && predicate && !predicate.apply(next)) {
                next = null
            }
        }

        prev
    }
}
