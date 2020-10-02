package ca.nicbo.eventapi.handler;

import ca.nicbo.eventapi.priority.EventPriority;
import ca.nicbo.eventapi.listener.Listener;

import java.lang.reflect.Method;

/**
 * Holds all data about an event handler
 *
 * @author Nicbo
 */

public final class Handler {
    private final Listener listener;
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
    public Handler(Listener listener, Method method, EventHandler annotation) {
        this.listener = listener;
        this.method = method;
        this.annotation = annotation;
    }

    public Listener getListener() {
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

    public EventHandler getAnnotation() {
        return annotation;
    }
}
