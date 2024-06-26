package redemption.server.server;

import java.nio.ByteBuffer;

import redemption.server.event.EventDecoder;
import redemption.server.event.GameEvent;
import redemption.server.event.impl.NetworkEvent;
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
public class Session {
    // Server attributes
    /**
     * {@link GameClient} associated to this Session.
     */
    private GameClient client;

    // Game attributes
    /**
     * {@link Player} associated to this Session.
     */
    private Player player;
    /**
     * Is the client playing ?
     */
    private boolean inGame;
    /**
     * {@link GameController} associated to this Session.
     */
    private GameController controller;

    public Session(GameClient c) {
        client = c;
        inGame = false;
    }

    /**
     * Connects {@link #client} to a controller and creates him a player.
     * 
     * @param ctr (GameController) game instance where the client is playing.
     * @throws Exception if the client is already in another game.
     */
    public void connectToGame(GameController ctr) throws Exception {
        if (inGame)
            throw new Exception();
        inGame = true;
        player = new Player(this);
        controller = ctr;
        controller.addSession(this);
    }

    /**
     * Disconnects {@link #client} from {@link #controller}.
     */
    public void disconnectFromGame() {
        inGame = false;
        controller.removeSession(this);
        controller = null;
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
     * Transform data into a {@link GameEvent}. According to {@link #inGame},
     * the event will then be handled either by the {@link #controller}, or
     * by a {@link Network} function.
     * 
     * @param buffer (ByteBuffer) data to process.
     * @return (GameEvent) the newly created event.
     */
    public void getEvent(ByteBuffer buffer) {
        // On génère l'event selon le paquet
        Event event = EventDecoder.decode(buffer);
        event.setSession(this);
        if (event instanceof GameEvent)
            controller.receiveEvent((GameEvent)event);
        else if (event instanceof NetworkEvent)
            Network.handleEvent((NetworkEvent)event);
        else {
            System.out.println("PAQUET PAS HANDLED");
            System.out.println("DEAD (cf Session) T_T");
            System.exit(42);
        }
    }

    /**
     * Pue globalement le kk car laisse trop de pouvoir côté client + tout aussi chiant à implémenter et/ou pas plus rapide 
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
     * Sends data back to {@link #client}.
     * 
     * @param buffer (ByteBuffer) data to send back.
     */
    public void sendToClient(ByteBuffer buffer) {
        client.send(buffer.array());
    }

    /**
     * Getter of {@link #player}.
     * 
     * @return (Player) player associated to this Session.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter of {@link #client}.
     * 
     * @return (GameClient) client associated to this Session.
     */
    public GameClient getClient() {
        return client;
    }
}
