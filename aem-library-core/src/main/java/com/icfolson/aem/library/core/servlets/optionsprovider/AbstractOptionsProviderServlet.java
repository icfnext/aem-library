package com.icfolson.aem.library.core.servlets.optionsprovider;

import com.icfolson.aem.library.api.request.ComponentServletRequest;
import com.icfolson.aem.library.core.servlets.AbstractComponentServlet;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base class for providing a list of "options" to a component dialog widget.  An option is simply a text/value pair to
 * be rendered in a selection box.  The implementing class determines how these options are retrieved from the
 * repository (or external provider, such as a web service).
 */
public abstract class AbstractOptionsProviderServlet extends AbstractComponentServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Get a list of "options" (text/value pairs) for rendering in an authoring dialog.  Building the list of options is
     * handled by the implementing class and will vary depending on the requirements for the component dialog calling
     * this servlet.
     *
     * @param request component servlet request
     * @return list of options as determined by the implementing class
     */
    protected abstract List<Option> getOptions(final ComponentServletRequest request);

    /**
     * @param request component servlet request
     * @return Optional name of root JSON object containing options
     */
    protected abstract Optional<String> getOptionsRoot(final ComponentServletRequest request);

    protected final void processGet(final ComponentServletRequest request) throws ServletException, IOException {
        final List<Option> options = getOptions(request);

        checkNotNull(options, "option list must not be null");

        final Optional<String> optionsRoot = getOptionsRoot(request);

        final SlingHttpServletResponse slingResponse = request.getSlingResponse();

        if (optionsRoot.isPresent()) {
            writeJsonResponse(slingResponse, ImmutableMap.of(optionsRoot.get(), options));
        } else {
            writeJsonResponse(slingResponse, options);
        }
    }
}
