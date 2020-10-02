package ca.nicbo.eventapi.priority;

/**
 * The priority that determines the order of event handlers
 * If two event handlers have the same priority it is
 * unknown as of which one will be fired first
 *
 * The order is as follows:
 *
 * LOWEST
 * LOW
 * MEDIUM
 * HIGH
 * HIGHEST
 * MONITOR (this should only be used for monitoring the event, not altering it)
 *
 * @author Nicbo
 */

public enum EventPriority {
    LOWEST,
    LOW,
    MEDIUM,
    HIGH,
    HIGHEST,
    MONITOR
}
