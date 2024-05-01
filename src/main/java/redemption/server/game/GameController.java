package redemption.server.game;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import redemption.server.event.EventEncoder;
import redemption.server.event.GameEvent;
import redemption.server.game.actors.Player;
import redemption.server.server.GameServer;
import redemption.server.server.Session;
import redemption.server.utilities.Utilities;

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

    private Queue<GameEvent> eventQueue;

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
    public void receiveEvent(GameEvent event) {
        eventQueue.add(event);
    }

    public void processRound() {
        Set<Actor> modifiedActors = new HashSet<>();
        while (!eventQueue.isEmpty()) {
            modifiedActors.addAll(eventQueue.poll().processEvent(this));
        }
        // MegaPacket containing all the states
        ByteBuffer buffer = Utilities.newBuffer();
        EventEncoder.addState(buffer, modifiedActors);
        sessions.forEach((s) -> s.sendToClient(buffer));
    }

    /**
     * Look over all the {@link Session} in {@link #sessions} list to find the
     * player with the specified UUID.
     * 
     * @param uuid (UUID) uuid of the wanted player
     * @return ({@link Player}) player corresponding to the specified UUID, if this
     *         player is in this controller. Returns null if no Player corresponding
     *         to the specified UUID is found.
     */
    public Player findPlayerByUUID(UUID uuid) {
        List<Player> players = sessions.stream().map(Session::getPlayer).collect(Collectors.toList());
        for (Player p : players) {
            if (p.getUUID().equals(uuid))
                return p;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        GameController ctr1 = new GameController(null);
        GameController ctr2 = new GameController(null);
        Session session1 = new Session(null);
        Session session2 = new Session(null);
        session1.connectToGame(ctr1);
        session2.connectToGame(ctr2);
        session1.getPlayer().setUUID(UUID.fromString("9ef44d92-e203-44dc-8a5d-538638775be3"));
        session2.getPlayer().setUUID(UUID.fromString("b15517f9-d98c-434b-a1b7-0467816901b0"));
        System.out.println(ctr1.findPlayerByUUID(UUID.fromString("9ef44d92-e203-44dc-8a5d-538638775be3")));
        System.out.println(ctr2.findPlayerByUUID(UUID.fromString("9ef44d92-e203-44dc-8a5d-538638775be3")));

    }
}
