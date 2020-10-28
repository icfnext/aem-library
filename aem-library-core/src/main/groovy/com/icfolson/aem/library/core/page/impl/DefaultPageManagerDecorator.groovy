package com.icfolson.aem.library.core.page.impl

import com.day.cq.commons.jcr.JcrConstants
import com.day.cq.replication.ReplicationStatus
import com.day.cq.tagging.TagManager
import com.day.cq.wcm.api.Page
import com.day.cq.wcm.api.PageManager
import com.day.cq.wcm.api.WCMException
import com.google.common.base.Predicate
import com.google.common.base.Stopwatch
import com.icfolson.aem.library.api.page.PageDecorator
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.core.page.predicates.TemplatePredicate
import com.icfolson.aem.library.core.utils.PathUtils
import groovy.util.logging.Slf4j
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver

import javax.jcr.RepositoryException
import javax.jcr.query.Query
import javax.jcr.query.Row

import static com.google.common.base.Preconditions.checkNotNull
import static java.util.concurrent.TimeUnit.MILLISECONDS

@Slf4j("LOG")
class DefaultPageManagerDecorator implements PageManagerDecorator {

    private final ResourceResolver resourceResolver

    @Delegate
    private final PageManager pageManager

    DefaultPageManagerDecorator(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver

        pageManager = resourceResolver.adaptTo(PageManager)
    }

    @Override
    PageManager getPageManager() {
        return pageManager;
    }

    @Override
    List<PageDecorator> findPages(String rootPath, Collection<String> tagIds, boolean matchOne) {
        checkNotNull(rootPath)
        checkNotNull(tagIds)

        LOG.debug("path = {}, tag IDs = {}", rootPath, tagIds)

        def stopwatch = Stopwatch.createStarted()

        def iterator = resourceResolver.adaptTo(TagManager).find(rootPath, tagIds as String[], matchOne)

        def pages = []

        iterator*.each { resource ->
            if (JcrConstants.JCR_CONTENT.equals(resource.name)) {
                def page = getPage(resource.parent.path)

                if (page) {
                    pages.add(page)
                }
            }
        }

        LOG.debug("found {} result(s) in {}ms", pages.size(), stopwatch.elapsed(MILLISECONDS))

        pages
    }

    @Override
    List<PageDecorator> search(Query query) {
        search(query, -1)
    }

    @Override
    List<PageDecorator> search(Query query, int limit) {
        checkNotNull(query)

        LOG.debug("query statement = {}", query.statement)

        def stopwatch = Stopwatch.createStarted()

        def pages = []

        int count = 0

        try {
            def paths = [] as Set

            query.execute().rows.each { Row row ->
                if (limit == -1 || count < limit) {
                    def path = row.path

                    LOG.debug("result path = {}", path)

                    def pagePath = PathUtils.getPagePath(path)

                    // ensure no duplicate pages are added
                    if (!paths.contains(pagePath)) {
                        paths.add(pagePath)

                        def page = getPageDecorator(path)

                        if (page) {
                            pages.add(page)
                            count++
                        } else {
                            LOG.error("result is null for path = {}", path)
                        }
                    }
                }
            }

            stopwatch.stop()

            LOG.debug("found {} result(s) in {}ms", pages.size(), stopwatch.elapsed(MILLISECONDS))
        } catch (RepositoryException re) {
            LOG.error("error finding pages for query = ${query.statement}", re)
        }

        pages
    }

    @Override
    List<PageDecorator> findPages(String rootPath, String template) {
        findPages(rootPath, new TemplatePredicate(template))
    }

    @Override
    List<PageDecorator> findPages(String rootPath, Predicate<PageDecorator> predicate) {
        def page = getPage(checkNotNull(rootPath))

        def stopwatch = Stopwatch.createStarted()

        def result = page ? page.findDescendants(predicate) : []

        stopwatch.stop()

        LOG.debug("found {} result(s) in {}ms", result.size(), stopwatch.elapsed(MILLISECONDS))

        result
    }

    @Override
    Page getContainingPage(Resource resource) {
        //decorate(pageManager.getContainingPage(resource))
        null
    }

    @Override
    Page getContainingPage(String path) {
        //decorate(pageManager.getContainingPage(path))
        null
    }

    @Override
    PageDecorator getPage(Page page) {
        page.adaptTo(Resource).resourceResolver.adaptTo(PageDecorator)
        return Optional.ofNullable(page)
                .map(p -> p.adaptTo(PageDecorator.class))
                .orElse(null);
        //decorate(page)


    }

    @Override
    Page getPage(String path) {
        //getPageDecorator(checkNotNull(path))
        null
    }

    // internals
    PageDecorator getPageDecorator(String path) {
        decorate(pageManager.getPage(path))
    }

    private static PageDecorator decorate(Page page) {
        return new DefaultPageDecorator(page)
    }
}
