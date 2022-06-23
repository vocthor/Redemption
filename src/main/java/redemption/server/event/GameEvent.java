package redemption.server.event;

import java.util.List;

import redemption.server.game.Actor;
import redemption.server.game.GameController;
import redemption.server.game.actors.Player;
import redemption.server.server.Session;

/**
 * An GameEvent represents an action within the game or the server.
 */
public abstract class GameEvent {
    /**
     * Type of the event.
     * 
     * @see {@link EventType}.
     */
    protected int type;
    /**
     * {@link Session} associated to this event.
     */
    protected Session session;

    public GameEvent(int t) {
        type = t;
    }

    /**
     * Getter of {@link GameEvent#type}.
     * 
     * @return (EventType) type associated to this event.
     */
    public int getType() {
        return type;
    }

    /**
     * Setter of {@link GameEvent#type}.
     * 
     * @param type (EventType) type associated to this event.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Getter of the {@link Player} associated to {@link GameEvent#session}.
     * 
     * @return (Player) player associated to the session associated to this event.
     * @see {@link Session#getPlayer()}.
     */
    public Player getPlayer() {
        return session.getPlayer();
    }

    /**
     * Processes the event according to a certain {@link GameController}.
     * 
     * @param controller (GameController) controller
     * @return (List of {@link Actor}) actors modified by this event.
     */
    public abstract List<? extends Actor> processEvent(GameController controller);

    /**
     * Getter of {@link GameEvent#session}.
     * 
     * @return (Session) session associated to this event.
     */
    public Session getSession() {
        return session;
    }

    /**
     * Setter of {@link GameEvent#session}.
     * 
     * @param s (Session) session associated to this event.
     */
    public void setSession(Session s) {
        session = s;
    }

    public String toString() {
        return "Type : " + type;
    }
}
