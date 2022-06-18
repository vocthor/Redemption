package redemption.server.game;

import java.util.ArrayList;
import java.util.List;

import redemption.server.event.GameEvent;
import redemption.server.server.GameServer;
import redemption.server.server.Session;

/**
 * Controller of the game 'Redemption'. Each controller should be specific to
 * the game / logic you want.
 */
public class GameController {
    /**
     * List of {@link Session} connected to this controller.
     */
    private List<Session> sessions;
    /**
     * {@link GameServer} from where this controller is.
     */
    private GameServer server;

    public GameController(GameServer s) {
        sessions = new ArrayList<>();
        server = s;
    }

    /**
     * Adds a {@link Session} to {@link GameController#sessions}.
     * 
     * @param s (Session) session to add.
     */
    public void addSession(Session s) {
        sessions.add(s);
    }

    /**
     * Removes a {@link Session} from {@link GameController#sessions}.
     * 
     * @param s (Session) session to remove.
     */
    public void removeSession(Session s) {
        sessions.remove(s);
    }

    /**
     * Handle a {@link GameEvent} and process it.
     * 
     * @see {@link GameEvent#processEvent(GameController)}
     * @param event (GameEvent) event to process.
     */
    public void handleEvent(GameEvent event) {
        List<Actor> modifiedActors = event.processEvent(this);
        modifiedActors.forEach((a) -> {
            sessions.forEach((s) -> s.sendToClient(a.getState()));
        });
    }
}
