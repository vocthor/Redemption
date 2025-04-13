package redemption.server.event;

import lombok.Getter;
import lombok.Setter;
import redemption.server.server.Session;

/**
 * An Event represents an action within the game or the server.
 */
public abstract class Event {
    /**
     * {@link Session} associated to this event.
     */
    @Getter
    @Setter
    protected Session session;

    public Event() {
    }

    public String toString() {
        return "Event " + this.getClass().getName();
    }
}
