package redemption.server.event;

import redemption.server.server.Session;

/**
 * An Event represents an action within the game or the server.
 */
public abstract class Event {
    /**
     * {@link Session} associated to this event.
     */
    protected Session _session;

    public Event() {
    }

    /**
     * Getter of {@link GameEvent#_session}.
     * 
     * @return (Session) session associated to this event.
     */
    public Session getSession() {
        return _session;
    }

    /**
     * Setter of {@link GameEvent#_session}.
     * 
     * @param s (Session) session associated to this event.
     */
    public void setSession(Session s) {
        _session = s;
    }

    public String toString() {
        return "Event " + this.getClass().getName();
    }
}
