package ca.nicbo.eventapi.example;

import ca.nicbo.eventapi.event.Cancellable;
import ca.nicbo.eventapi.event.Event;

/**
 * An example event for the EventAPI
 *
 * @author Nicbo
 */

public class ExampleEvent implements Event, Cancellable {
    private String value;
    private boolean cancelled;

    public ExampleEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        System.out.println("ExampleEvent's value was set to " + value + ".");
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
