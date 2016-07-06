package com.icfolson.aem.library.core.servlets;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Base servlet for writing a JSON response.
 */
public abstract class AbstractJsonResponseServlet extends SlingAllMethodsServlet {

    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy hh:mm aaa z";

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJsonResponseServlet.class);

    private static final JsonFactory FACTORY = new JsonFactory().disable(Feature.AUTO_CLOSE_TARGET);

    private static final MediaType MEDIA_TYPE = MediaType.JSON_UTF_8;

    private static final String ENCODING = MEDIA_TYPE.charset().get().name();

    private static final String CONTENT_TYPE = MEDIA_TYPE.withoutParameters().toString();

    private static final long serialVersionUID = 1L;

    /**
     * Write an object to the response as JSON.
     *
     * @param response sling response
     * @param object object to be written as JSON
     * @throws IOException if error occurs writing JSON response
     */
    protected final void writeJsonResponse(final SlingHttpServletResponse response, final Object object)
        throws IOException, ServletException {
        writeJsonResponse(response, object, DEFAULT_DATE_FORMAT, Locale.US);
    }

    /**
     * Write an object to the response as JSON.
     *
     * @param response sling response
     * @param object object to be written as JSON
     * @param dateFormat SimpleDateFormat pattern for formatting Date objects using US locale
     * @throws IOException if error occurs writing JSON response
     */
    protected final void writeJsonResponse(final SlingHttpServletResponse response, final Object object,
        final String dateFormat) throws IOException, ServletException {
        writeJsonResponse(response, object, dateFormat, Locale.US);
    }

    /**
     * Write an object to the response as JSON.
     *
     * @param response sling response
     * @param object object to be written as JSON
     * @param dateFormat SimpleDateFormat pattern for formatting Date objects
     * @param locale locale for date format
     * @throws IOException if error occurs writing JSON response
     */
    protected final void writeJsonResponse(final SlingHttpServletResponse response, final Object object,
        final String dateFormat, final Locale locale) throws IOException, ServletException {
        final SimpleDateFormat format = new SimpleDateFormat(dateFormat, locale);

        writeJsonResponse(response, new ObjectMapper().setDateFormat(format), object);
    }

    /**
     * Write an object to the response as JSON using the given <code>ObjectMapper</code> instance.
     *
     * @param response Sling response
     * @param mapper object mapper with a custom configuration
     * @param object object to be written as JSON
     * @throws IOException if error occurs writing JSON response
     */
    protected final void writeJsonResponse(final SlingHttpServletResponse response, final ObjectMapper mapper,
        final Object object) throws IOException, ServletException {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING);

        try {
            final JsonGenerator generator = FACTORY.createGenerator(response.getWriter());

            mapper.writeValue(generator, object);
        } catch (IOException e) {
            LOG.error("error writing JSON response", e);

            throw new ServletException(e);
        }
    }
}
