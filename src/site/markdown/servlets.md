## Servlets

### Abstract JSON Response Servlet

`com.icfolson.aem.library.core.servlets.AbstractJsonResponseServlet`

Servlets should extend this class when writing a JSON response.  Objects passed to any of the `writeJsonResponse` methods will be serialized to the response writer using the [Jackson](https://github.com/FasterXML/jackson-databind) data binding library.

### Abstract Options Provider Servlet

`com.icfolson.aem.library.core.servlets.optionsprovider.AbstractOptionsProviderServlet`

Base class for providing a list of "options" to a component dialog widget.  An option is simply a text/value pair to be rendered in a selection box.  The implementing class determines how these options are retrieved from the repository (or external provider, such as a web service).

### Abstract Options Data Source Servlet

`com.icfolson.aem.library.core.servlets.datasource.AbstractOptionsDataSourceServlet`

Base class for supplying a data source to component dialogs using the Touch UI.  Implementing classes will provide a list of options that will be made available as text/value pairs to selection dialog elements.  Servlets must be annotated with the `@SlingServlet(resourceTypes = "projectname/datasources/colors")` annotation.  The resource type attribute is an arbitrary relative path that can be referenced by dialog elements using the data source.  The implementing class determines how these options are retrieved from the repository (or external provider, such as a web service).

### Abstract Validation Servlet

`com.icfolson.aem.library.core.servlets.AbstractValidationServlet`

Base class for validating component dialog fields.  Validation business logic is delegated to the extending class via the abstract `isValid()` method.

Servlets extending this class should be annotated with the `@SlingServlet` annotation:

    @SlingServlet(resourceTypes = "projectname/components/content/example", selectors = "validator", extensions = "json", methods = "GET")

The component dialog can then make a request to the the validator by defining validator function for the dialog field.

### Image Servlet

`com.icfolson.aem.library.core.servlets.ImageServlet`

The image servlet overrides AEM's default image rendering servlets to provide image resizing and the ability to associate additional named images to a page or component.

For additional details, see the [Image Rendering](https://github.com/OlsonDigital/aem-library/wiki/image-rendering) page.

### Paragraph JSON Servlet

`com.icfolson.aem.library.core.servlets.paragraphs.ParagraphJsonServlet`

### Selective Replication Servlet

`com.icfolson.aem.library.core.servlets.replication.SelectiveReplicationServlet`

This servlet is exposed via the `/bin/replicate/selective` path, which can be called from a JavaScript function to trigger a replication action to a specific set of Replication Agents.

    var path1 = '/content/dam';
    var path2 = '/content/en';

    var params = {
        paths: [path1, path2],
        action: 'ACTIVATE',
        agentIds: ['staging', 'publish']
    };

    $.post('/bin/replicate/selective', $.param(params, true), function (data) {
        if (data[0][path1] == true) {
            alert('Successfully activated ' + path1);
        }

        if (data[1][path2] == true) {
            alert('Successfully activated ' + path2);
        }
    });