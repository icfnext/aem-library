package com.icfolson.aem.library.core.servlets.datasource

import com.adobe.granite.ui.components.ds.DataSource
import com.day.cq.commons.Filter
import com.day.cq.tagging.Tag
import com.icfolson.aem.library.core.servlets.optionsprovider.Option
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import org.apache.sling.api.SlingHttpServletRequest

class TagDataSourceServletSpec extends AemLibrarySpec {

    static final def MAP = ["lager": "Lager", "stout": "Stout", "porter": "Porter", "ale": "Ale"]

    static final def OPTIONS = Option.fromMap(MAP)

    class BasicTagDataSourceServlet extends AbstractTagDataSourceServlet {

        @Override
        protected String getNamespace() {
            "beers:"
        }
    }

    class FilteredTagDataSourceServlet extends AbstractTagDataSourceServlet {

        @Override
        protected String getNamespace() {
            "beers:"
        }

        @Override
        protected Filter<Tag> getTagFilter() {
            new Filter<Tag>() {
                @Override
                boolean includes(Tag tag) {
                    tag.title == "Lager" || tag.title == "Ale"
                }
            }
        }
    }

    def setupSpec() {
        nodeBuilder.content {
            "cq:tags" {
                beers("cq:Tag", "sling:resourceType": "cq/tagging/components/tag", "jcr:title": "Beers") {
                    lager("cq:Tag", "sling:resourceType": "cq/tagging/components/tag", "jcr:title": "Lager")
                    stout("cq:Tag", "sling:resourceType": "cq/tagging/components/tag", "jcr:title": "Stout")
                    porter("cq:Tag", "sling:resourceType": "cq/tagging/components/tag", "jcr:title": "Porter")
                    ale("cq:Tag", "sling:resourceType": "cq/tagging/components/tag", "jcr:title": "Ale")
                }
            }
        }
    }

    def "all tags options"() {
        def servlet = new BasicTagDataSourceServlet()
        def request = requestBuilder.build()
        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        assertDataSourceOptions(request, OPTIONS)
    }

    def "filtered tags options"() {
        def servlet = new FilteredTagDataSourceServlet()
        def request = requestBuilder.build()
        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        assertDataSourceOptions(request, Option.fromMap(["lager": "Lager", "ale": "Ale"]))
    }

    void assertDataSourceOptions(SlingHttpServletRequest request, List<Option> options) {
        def dataSource = request.getAttribute(DataSource.name) as DataSource
        def resources = dataSource.iterator()

        assert resources.size() == options.size()

        resources.eachWithIndex { resource, i ->
            def properties = resource.valueMap
            def option = options.get(i)

            assert option.text == properties.get("text", "")
            assert option.value == properties.get("value", "")
        }
    }
}
