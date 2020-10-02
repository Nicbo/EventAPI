package ca.nicbo.eventapi.event;

/**
 * Implement this in events that are cancellable
 *
 * @author Nicbo
 */

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
