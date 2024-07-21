package redemption.server.server;

import java.nio.ByteBuffer;

import lombok.extern.log4j.Log4j2;
import redemption.server.event.EventDecoder;
import redemption.server.event.GameEvent;
import redemption.server.event.NetworkEvent;
import redemption.server.event.Event;
import redemption.server.game.GameController;
import redemption.server.game.actors.Player;
import redemption.server.utilities.Utilities;

/**
 * A Session is an intermadiate class between {@link GameClient} and
 * {@link GameController} used to store and process data.
 * 
 * @see {@link GameClient}.
 * @see {@link GameController}.
 */
@Log4j2
public class Session {
    // Server attributes
    /**
     * {@link GameClient} associated to this Session.
     */
    private GameClient _client;

    // Game attributes
    /**
     * {@link Player} associated to this Session.
     */
    private Player _player;
    /**
     * Is the client playing ?
     */
    private boolean _inGame;
    /**
     * {@link GameController} associated to this Session.
     */
    private GameController _controller;

    public Session(GameClient c) {
        log.info("Creating new Session.");
        _client = c;
        _inGame = false;
    }

    /**
     * Connects {@link #_client} to a controller and creates him a player.
     * 
     * @param ctr (GameController) game instance where the client is playing.
     * @throws Exception if the client is already in another game.
     */
    public void connectToGame(GameController ctr) throws Exception {
        log.info("Connecting Session to GameController.");
        if (_inGame) {
            log.info("Could not connect a Session that is already connected to a GameController !");
            throw new Exception();
        }
        _inGame = true;
        _player = new Player(this);
        _controller = ctr;
        _controller.addSession(this);
        log.info("Session successfuly connected to GameController.");
    }

    /**
     * Disconnects {@link #_client} from {@link #_controller}.
     */
    public void disconnectFromGame() {
        log.info("Disconnecting Session from GameController.");
        _inGame = false;
        _controller.removeSession(this);
        _controller = null;
    }

    public void close() {
        disconnectFromGame();
    }

    /**
     * Wrap the param into a ByteBuffer, and sends it to
     * {@link #getEvent(ByteBuffer)}.
     * 
     * @param buffer (byte[]) buffer.
     */
    public void getEvent(byte[] buf) {
        ByteBuffer buffer = Utilities.newBuffer(buf);
        getEvent(buffer);
    }

    /**
     * Transform data into a {@link GameEvent}. According to {@link #_inGame},
     * the event will then be handled either by the {@link #_controller}, or
     * by a {@link Network} function.
     * 
     * @param buffer (ByteBuffer) data to process.
     * @return (GameEvent) the newly created event.
     */
    public void getEvent(ByteBuffer buffer) {
        // On génère l'event selon le paquet
        Event event = EventDecoder.decode(buffer);
        if (event == null) {
            log.warn("Event is null.");
            return;
        }
        event.set_session(this);
        if (event instanceof GameEvent)
            _controller.receiveEvent((GameEvent) event);
        else if (event instanceof NetworkEvent)
            Network.receiveEvent((NetworkEvent) event);
        else {
            log.fatal("WTF EVENT OVNI");
            System.out.println("PAQUET PAS HANDLED");
            System.out.println("DEAD (cf Session) T_T");
            System.exit(42);
        }
    }

    /**
     * Pue globalement le kk car laisse trop de pouvoir côté client + tout aussi
     * chiant à implémenter et/ou pas plus rapide
     * 
     * @see{https://github.com/f0rbit/gm-server/blob/main/src/main/java/dev/forbit/server/utilities/Utilities.java}
     * @return
     */
    // public static Optional<GameEvent> getPacket(String header) {
    // try {
    // Class<?> clazz = Class.forName(header);
    // var event = (GameEvent) clazz.getDeclaredConstructor().newInstance();
    // return Optional.of((GameEvent) clazz.getDeclaredConstructor().newInstance());
    // } catch (Exception exception) {
    // return Optional.empty();
    // }
    // }

    /**
     * Sends data back to {@link #_client}.
     * 
     * @param buffer (ByteBuffer) data to send back.
     */
    public void sendToClient(ByteBuffer buffer) {
        log.info("Sending back " + buffer.array());
        _client.send(buffer.array());
    }

    /**
     * Getter of {@link #_player}.
     * 
     * @return (Player) player associated to this Session.
     */
    public Player get_player() {
        return _player;
    }

    /**
     * Getter of {@link #_client}.
     * 
     * @return (GameClient) client associated to this Session.
     */
    public GameClient get_client() {
        return _client;
    }
}
