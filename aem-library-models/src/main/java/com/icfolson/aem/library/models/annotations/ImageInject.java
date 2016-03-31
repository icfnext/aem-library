package com.icfolson.aem.library.models.annotations;

import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@InjectAnnotation
@Source(ImageInject.NAME)
public @interface ImageInject {

    String NAME = "images";

    String SELF = ".";

    String IMG_SELECTOR = "img";

    /**
     * if set to REQUIRED injection is mandatory, if set to OPTIONAL injection is optional, in case of DEFAULT the
     * standard annotations ( {@link org.apache.sling.models.annotations.Optional}, {@link
     * org.apache.sling.models.annotations.Required}) are used. If even those are not available the default injection
     * strategy defined on the {@link org.apache.sling.models.annotations.Model} applies. Default value = DEFAULT.
     *
     * @return Injection strategy
     */
    InjectionStrategy injectionStrategy() default InjectionStrategy.DEFAULT;

    /**
     * Whether the image should be attempted to be resolved from the root of the resource
     *
     * @return true if is self
     */
    boolean isSelf() default false;

    /**
     * Whether to get the link via inheriting
     *
     * @return true if is inherited
     */
    boolean inherit() default false;

    /**
     * Selector to set on the injected Image object. This affects the calculated source of the image. Defaults to img as
     * this selector will trigger the OOB ImageServlet and is usually the selector you want.
     *
     * @return selectors
     */
    String[] selectors() default { IMG_SELECTOR };

}
