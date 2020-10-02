# EventAPI
An extremely lightweight API that can be used for event based java projects. It is heavily inspired by [DarkMagician6's EventAPI](https://bitbucket.org/DarkMagician6/eventapi/src/master/) and the [Spigot](https://www.spigotmc.org/) event system. It is a very simplified version of what theirs have to offer. You are free to alter and use this however you like as it is licensed under the MIT License. I am probably going to keep this updated until I'm happy with it and there are no bugs.

### Creating an event
To create an event you must implement [Event](src/ca/nicbo/eventapi/event/Event.java) and if you want it to be cancellable implement [Cancellable](src/ca/nicbo/eventapi/event/Cancellable.java).

```java
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
```

### Creating a listener
To create a listener for events your class must implement [Listener](src/ca/nicbo/eventapi/listener/Listener.java). For an event handler to work you must add the [@EventHandler](src/ca/nicbo/eventapi/handler/EventHandler.java) annotation to the method and have one parameter that can be an instance of [Event](src/ca/nicbo/eventapi/event/Event.java).

The [@EventHandler](src/ca/nicbo/eventapi/handler/EventHandler.java) annotation has some optional parameters. The [EventPriority](src/ca/nicbo/eventapi/priority/EventPriority.java) is the order that the event handlers are called (LOWEST, LOW, MEDIUM, HIGH, HIGHEST, MONITOR). EventPriority.MONITOR should only be used to monitor the event and should not alter the event in any way. The ignoreCancelled boolean decides if your event handler should be called when an event is cancelled. The priority defaults to EventPriority.MEDIUM and the ignoreCancelled defaults to false.

```java
public class ExampleListener implements Listener {
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
```

### Bringing it all together
To have the listeners and events actually work you are going to need to do a few things.

```java
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
```

This will output:
```
ExampleEvent's value was set to LOWEST.
ExampleEvent's value was set to MEDIUM.
The final value of ExampleEvent is MEDIUM and the event is cancelled.
Don't do something!
```

### Some final notes
There are a couple final things I should mention. First off, this API was not programmed with thread safety in mind. If you are accessing it from multiple threads then you will need to add your own safety measures.

Lastly, if an event handler is listening to the superclass or interface of an event that is fired, the event handler will be called. This means that any event handlers that are listening to superclasses of events would be called when the subclass events are called.

```java
public class PlayerEvent implements Event {
    private final Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;    
    }   
}
```

```java
public class PlayerMoveEvent extends PlayerEvent {
    private final Location to;  
    private final Location from;

    public PlayerMoveEvent(Player player, Location to, Location from) {
        super(player);
        this.to = to;
        this.from = from;
    }   

    public Location getTo() {
        return to;
    }

    public Location getFrom() {
        return from;
    }
}
```

This event handler would be called when PlayerMoveEvent is fired.

```java
public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerEvent(PlayerEvent event) {
        // This will be called by any event that is an instance of PlayerEvent
        System.out.println("A PlayerEvent was fired!");
    }
}
```
