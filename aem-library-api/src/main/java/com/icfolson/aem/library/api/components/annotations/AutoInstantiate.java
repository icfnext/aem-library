package com.icfolson.aem.library.api.components.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for components that can be automatically instantiated by the {@code <aem-library:defineObjects/>} in a component
 * JSP. If the component's <code>.content.xml</code> has a <code>className</code> attribute value set to a valid
 * component Java class name, the class will be instantiated and set in page context without the need to use the {@code
 * <aem-library:component/>} tag.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface AutoInstantiate {

    /**
     * Specify an instance name to use when the component class is set in page context.
     *
     * @return instance name to use instead of the default (uncapitalized simple class name).
     */
    String instanceName() default "";
}
