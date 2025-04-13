package redemption.server.event;

import java.util.List;

import redemption.server.game.Actor;
import redemption.server.game.GameController;
import redemption.server.game.actors.Player;
import redemption.server.server.Session;

/**
 * An GameEvent represents an action within the game or the server.
 */
public abstract class GameEvent extends Event {
    public GameEvent() {
        super();
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

    @Override
    public String toString() {
        return "Event : " + this.getClass().getName();
    }
}
