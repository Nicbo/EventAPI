package ca.nicbo.eventapi.handler;

import ca.nicbo.eventapi.priority.EventPriority;

import java.lang.reflect.Method;

/**
 * Holds all data about an event handler
 *
 * @author Nicbo
 */

public class Handler {
    private final Object listener;
    private final Method method;
    private final EventHandler annotation;

    /**
     * Creates an instance of Handler
     *
     * @param listener the listener of which the handler belongs to
     * @param method the method that is the event handler
     * @param annotation the event handler annotation
     * @see EventHandler
     */
    public Handler(Object listener, Method method, EventHandler annotation) {
        this.listener = listener;
        this.method = method;
        this.annotation = annotation;
    }

    public Object getListener() {
        return listener;
    }

    public Method getMethod() {
        return method;
    }

    public EventPriority getPriority() {
        return annotation.priority();
    }

    public boolean isIgnoringCancelled() {
        return annotation.ignoreCancelled();
    }
}
