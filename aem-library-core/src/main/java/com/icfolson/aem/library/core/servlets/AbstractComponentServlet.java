package com.icfolson.aem.library.core.servlets;

import com.icfolson.aem.library.api.request.ComponentServletRequest;
import com.icfolson.aem.library.core.request.impl.DefaultComponentServletRequest;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Proxy servlet that wraps the Sling request in a "component" request for access to convenience accessor methods.
 */
public abstract class AbstractComponentServlet extends AbstractJsonResponseServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected final void doDelete(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
        throws ServletException, IOException {
        processDelete(new DefaultComponentServletRequest(request, response));
    }

    @Override
    protected final void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
        throws ServletException, IOException {
        processGet(new DefaultComponentServletRequest(request, response));
    }

    @Override
    protected final void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
        throws ServletException, IOException {
        processPost(new DefaultComponentServletRequest(request, response));
    }

    @Override
    protected final void doPut(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
        throws ServletException, IOException {
        processPut(new DefaultComponentServletRequest(request, response));
    }

    /**
     * Process a DELETE request.  Extending classes can optionally override this method to implement desired
     * functionality.
     *
     * @param request component request
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processDelete(final ComponentServletRequest request) throws ServletException, IOException {
        handleMethodNotImplemented(request.getSlingRequest(), request.getSlingResponse());
    }

    /**
     * Process a GET request.  Extending classes can optionally override this method to implement desired
     * functionality.
     *
     * @param request component request
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processGet(final ComponentServletRequest request) throws ServletException, IOException {
        handleMethodNotImplemented(request.getSlingRequest(), request.getSlingResponse());
    }

    /**
     * Process a POST request.  Extending classes can optionally override this method to implement desired
     * functionality.
     *
     * @param request component request
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processPost(final ComponentServletRequest request) throws ServletException, IOException {
        handleMethodNotImplemented(request.getSlingRequest(), request.getSlingResponse());
    }

    /**
     * Process a PUT request.  Extending classes can optionally override this method to implement desired
     * functionality.
     *
     * @param request component request
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processPut(final ComponentServletRequest request) throws ServletException, IOException {
        handleMethodNotImplemented(request.getSlingRequest(), request.getSlingResponse());
    }
}
