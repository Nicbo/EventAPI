package ca.nicbo.eventapi.handler;

import ca.nicbo.eventapi.priority.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method so that the EventManager knows
 * that it should be registered as an event handler
 *
 * @author Nicbo
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    EventPriority priority() default EventPriority.MEDIUM;
    boolean ignoreCancelled() default false;
}
