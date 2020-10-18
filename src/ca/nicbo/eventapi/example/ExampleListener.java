package ca.nicbo.eventapi.example;

import ca.nicbo.eventapi.handler.EventHandler;
import ca.nicbo.eventapi.priority.EventPriority;

/**
 * An example listener for the EventAPI
 *
 * Usually you would not have multiple handlers for the same event in the same listener
 * I've only done it here to show the behavior of event handlers
 *
 * @author Nicbo
 */

public class ExampleListener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onExampleEventLowest(ExampleEvent event) {
        event.setValue("LOWEST");
    }

    @EventHandler
    public void onExampleEventMedium(ExampleEvent event) {
        event.setCancelled(true);
        event.setValue("MEDIUM");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExampleEventHighest(ExampleEvent event) {
        event.setValue("HIGHEST");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onExampleEventMonitor(ExampleEvent event) {
        System.out.println("The final value of ExampleEvent is " + event.getValue() +
                " and the event is " + (event.isCancelled() ? "cancelled." : "not cancelled."));
    }
}
