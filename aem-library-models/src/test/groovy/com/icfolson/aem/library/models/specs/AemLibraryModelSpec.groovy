package com.icfolson.aem.library.models.specs

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import com.icfolson.aem.library.models.impl.AdaptableInjector
import com.icfolson.aem.library.models.impl.ComponentInjector
import com.icfolson.aem.library.models.impl.InheritInjector
import com.icfolson.aem.library.models.impl.LinkInjector
import com.icfolson.aem.library.models.impl.ModelListInjector
import com.icfolson.aem.library.models.impl.ReferenceInjector
import com.icfolson.aem.library.models.impl.ValueMapFromRequestInjector
import com.icfolson.aem.library.models.impl.EnumInjector
import com.icfolson.aem.library.models.impl.ImageInjector
import com.icfolson.aem.library.models.impl.TagInjector

/**
 * Specs may extend this class to support injection of AEM Library dependencies in Sling model-based components.
 */
abstract class AemLibraryModelSpec extends AemLibrarySpec {

    /**
     * Register default AEM Library injectors and all <code>@Model>/code>-annotated classes for the current package.
     */
    def setupSpec() {
        registerDefaultInjectors()

        slingContext.addModelsForPackage(this.class.package.name)
    }

    /**
     * Register the default set of AEM Library injector services.
     */
    void registerDefaultInjectors() {
        slingContext.with {
            registerInjector(new ComponentInjector(), Integer.MAX_VALUE)
            registerInjector(new AdaptableInjector(), Integer.MIN_VALUE)
            registerInjector(new TagInjector(), 800)
            registerInjector(new EnumInjector(), 4000)
            registerInjector(new ImageInjector(), 4000)
            registerInjector(new InheritInjector(), 4000)
            registerInjector(new LinkInjector(), 4000)
            registerInjector(new ReferenceInjector(), 4000)
            registerInjector(new ModelListInjector(), 999)
            registerInjector(new ValueMapFromRequestInjector(), 2500)
        }
    }
}
