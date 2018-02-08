package com.icfolson.aem.library.core.rollout.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ObjectClassDefinition(name = "AEM Library Rollout Inheritance Event Listener")
@Retention(RetentionPolicy.RUNTIME)
public @interface RolloutInheritanceEventListenerConfiguration {

    @AttributeDefinition(name = "Path Root")
    String pathRoot() default "";
}
