package redemption.server.game;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class GameController extends Thread {
    /**
     * List of {@link Session} connected to this controller.
     */
    private List<Session> _sessions;
    /**
     * {@link GameServer} from where this controller is.
     */
    private GameServer _server;

    private Queue<GameEvent> _eventQueue;

    private final long ROUND_DURATION = 15000;

    public GameController(GameServer s) {
        log.info("Creating new GameController.");
        _sessions = new ArrayList<>();
        _server = s;
        _eventQueue = new LinkedList<>();
    }

    /**
     * Main loop of the game
     */
    @Override
    public void run() {
        log.info("Starting GameController thread.");
        _eventQueue.clear();
        while (true) {
            log.info("New GameController loop.");
            processRound();
            try {
                sleep(ROUND_DURATION);
            } catch (InterruptedException e) {
                log.warn("GameContoller loop has been interrupted !", e);
            }
        }
    }

    /**
     * Handle a {@link GameEvent} and adds it to the queue.
     * 
     * @see {@link GameEvent#processEvent(GameController)}
     * @param event (GameEvent) event to process.
     */
    public synchronized void receiveEvent(GameEvent event) {
        log.info("New event received in the GameController. Adding it to the queue ...");
        _eventQueue.add(event);
        log.info("Event added to the GameController's queue.");
    }

    public synchronized void processRound() {
        // ! TODO : ON COURT A LA CACASTROPHE SI ON DEPASSE LA TAILLE DU BUFFER
        // * On a plrs solutions
        // 1 : faire un buffer par actor modifié (ca limitera mais le pb pourrait tjrs
        // survenir sur de gros acteurs)
        // 1.5 : faire un paquet par event process (permet un compris nombre / taille de
        // paquets ?)
        // 2 : passer localement à un ByteArrayOutputStream pour le reconvertir en un
        // ByteBuffer de la bonne taille
        // 3 : passer globalement à un ByteArrayOuputStream => changer tt le serveur et
        // la construction des paquets (chiant)
        // 4 : créer à la volée un nouveau paquet si besoin (pas beau + pas rentable)
        // ? Solution retenue (pour l'instant) : faire un paquet par acteur + augmenter
        // taille à 4096o MAX
        // Pour garder ordre (et effet visuel chez client) : 1 event recu => 1 paquet
        // renvoyé
        log.info("Processing the round events.");
        while (!_eventQueue.isEmpty()) {
            log.info("GameController's queue is not empty. Processing event one by one.");
            Set<Actor> modifiedActors = new HashSet<>(_eventQueue.poll().processEvent(this));
            log.info(modifiedActors.size() + " actors modified.");
            // Un paquet par acteur modifié à chaque event
            modifiedActors.forEach((a) -> {
                ByteBuffer buffer = Utilities.newBuffer();
                EventEncoder.addState(buffer, a);
                _sessions.forEach((s) -> s.sendToClient(buffer));
            });
        }
        log.info("All Events in queue processed.");
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
        log.info("Looking for player with uuid + " + uuid + " ...");
        List<Player> players = _sessions.stream().map(Session::getPlayer).collect(Collectors.toList());
        for (Player p : players) {
            if (p.getUUID().equals(uuid)) {
                log.info("Player with uuid " + uuid + "found : " + p.toString());
                return p;
            }
        }
        log.info("No player found with uuid " + uuid);
        return null;
    }

    /**
     * Adds a {@link Session} to {@link GameController#sessions}.
     * 
     * @param s (Session) session to add.
     */
    public void addSession(Session s) {
        _sessions.add(s);
    }

    /**
     * Removes a {@link Session} from {@link GameController#sessions}.
     * 
     * @param s (Session) session to remove.
     */
    public void removeSession(Session s) {
        _sessions.remove(s);
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
