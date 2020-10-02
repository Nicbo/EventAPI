package ca.nicbo.eventapi.example;

import ca.nicbo.eventapi.EventManager;

/**
 * Source to the EventAPI: https://github.com/Nicbo/EventAPI
 *
 * An example main class for the EventAPI
 *
 * @author Nicbo
 */

public class ExampleMain {
    public static void main(String[] args) {
        // Create instance of the event manager
        EventManager eventManager = new EventManager();

        // Create instance of the listener
        ExampleListener exampleListener = new ExampleListener();

        // Register the listener
        eventManager.registerListener(exampleListener);

        // Create instance of the event and call it
        ExampleEvent exampleEvent = eventManager.call(new ExampleEvent("Starting"));

        // Do logic based on if the event is cancelled
        if (exampleEvent.isCancelled()) {
            System.out.println("Don't do something!");
        } else {
            System.out.println("Do something!");
        }
    }
}