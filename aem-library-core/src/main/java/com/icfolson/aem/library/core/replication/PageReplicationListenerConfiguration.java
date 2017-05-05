package com.icfolson.aem.library.core.replication;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ObjectClassDefinition(name = "AEM Library Page Replication Listener")
@Retention(RetentionPolicy.RUNTIME)
public @interface PageReplicationListenerConfiguration {

    @AttributeDefinition(name = "Enabled?", description = "Enable this replication listener.")
    boolean enabled() default false;
}