package ca.nicbo.eventapi;

import ca.nicbo.eventapi.event.Cancellable;
import ca.nicbo.eventapi.event.Event;
import ca.nicbo.eventapi.handler.EventHandler;
import ca.nicbo.eventapi.handler.Handler;
import ca.nicbo.eventapi.listener.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Manages registering, unregistering and firing of events
 *
 * @author Nicbo
 */

public final class EventManager {
    private final Map<Class<? extends Event>, Set<Handler>> eventHandlers;

    public EventManager() {
        this.eventHandlers = new HashMap<>();
    }

    /**
     * Registers all of the event handlers in the listener
     *
     * @param listener the listener
     */
    public void registerListener(Listener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (isEventHandler(method)) {
                registerEventHandler(method, listener);
            }
        }
    }

    /**
     * Unregisters all of the event handlers in the listener
     *
     * @param listener the listener
     */
    public void unregisterListener(Listener listener) {
        for (Iterator<Set<Handler>> iterator = eventHandlers.values().iterator(); iterator.hasNext(); ) {
            Set<Handler> handlers = iterator.next();
            handlers.removeIf(handler -> handler.getListener().equals(listener));

            if (handlers.isEmpty()) {
                iterator.remove();
            }
        }
    }

    /**
     * Unregisters every event handler
     */
    public void unregisterAllListeners() {
        eventHandlers.clear();
    }

    /**
     * Fires an event to all event handlers that are registered
     * If the event handler is ignoring cancelled and the
     * event is cancelled then the handler will not be called
     *
     * @param event the event to fire
     * @param <T> the type of event
     * @return the event that has been modified by all of the handlers
     * @see Cancellable
     */
    public <T extends Event> T call(T event) {
        List<Handler> sortedHandlers = new ArrayList<>();

        Class<?> clazz = event.getClass();

        // Traverse the inheritance hierarchy adding all handlers until we reach Object.class
        while (clazz != Object.class) {
            // Check if class has handlers
            Set<Handler> classHandlers = eventHandlers.get(clazz);
            if (classHandlers != null) {
                sortedHandlers.addAll(classHandlers);
            }

            // Check if any implemented interfaces have handlers
            for (Class<?> i : clazz.getInterfaces()) {
                Set<Handler> interfaceHandlers = eventHandlers.get(i);
                if (interfaceHandlers != null) {
                    sortedHandlers.addAll(interfaceHandlers);
                }
            }

            clazz = clazz.getSuperclass();
        }

        // If this event actually has handlers
        if (!sortedHandlers.isEmpty()) {
            // Sort based on priorities
            sortedHandlers.sort(Comparator.comparing(Handler::getPriority));

            Cancellable cancellable = event instanceof Cancellable ? (Cancellable) event : null;

            for (Handler handler : sortedHandlers) {
                // If the event is cancellable, the handler is ignoringCancelled and the event is cancelled
                if (cancellable != null && handler.isIgnoringCancelled() && cancellable.isCancelled()) {
                    continue;
                }

                // Call the event handler
                invoke(handler, event);
            }
        }

        return event;
    }

    /**
     * Registers the method as an event handler
     *
     * @param method the method that is being registered
     * @param listener the listener of which the method belongs to
     */
    private void registerEventHandler(Method method, Listener listener) {
        Class<? extends Event> clazz = method.getParameterTypes()[0].asSubclass(Event.class);

        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        Handler handler = new Handler(listener, method, method.getAnnotation(EventHandler.class));
        eventHandlers.computeIfAbsent(clazz, k -> new HashSet<>()).add(handler);
    }

    /**
     * Checks if the given method is a valid event handler
     *
     * The method must have the EventHandler annotation
     * and have a singular parameter that is an instance of event
     *
     * @param method the method to check
     * @return true if the method is a valid event handler
     */
    private static boolean isEventHandler(Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        return parameters.length == 1 && Event.class.isAssignableFrom(parameters[0]) && method.isAnnotationPresent(EventHandler.class);
    }

    /**
     * Invokes the handlers method with the event as a parameter
     *
     * @param handler the handler whose method should be invoked
     * @param event the event that is being passed in
     */
    private static void invoke(Handler handler, Event event) {
        try {
            handler.getMethod().invoke(handler.getListener(), event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
