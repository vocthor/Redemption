package redemption.server.server;

import java.nio.ByteBuffer;
import java.util.Optional;

import redemption.server.event.EventDecoder;
import redemption.server.event.GameEvent;
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
     * Connects {@link Session#client} to a controller and creates him a player.
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
     * Disconnects {@link Session#client} from {@link Session#controller}.
     */
    public void disconnectFromGame() {
        inGame = false;
        controller = null;
        controller.removeSession(this);
    }

    /**
     * Wrap the param into a ByteBuffer, and sends it to
     * {@link Session#getEvent(ByteBuffer)}.
     * 
     * @param buffer (byte[]) buffer.
     */
    public void getEvent(byte[] buf) {
        ByteBuffer buffer = Utilities.newBuffer(buf);
        getEvent(buffer);
    }

    /**
     * Transform data into a {@link GameEvent}. According to {@link Session#inGame},
     * the event will then be handled either by the {@link Session#controller}, or
     * by a {@link Network} function.
     * 
     * @param buffer (ByteBuffer) data to process.
     * @return (GameEvent) the newly created event.
     */
    public GameEvent getEvent(ByteBuffer buffer) {
        // On génère l'event selon le paquet
        GameEvent event = EventDecoder.handle(buffer);
        event.setSession(this);
        if (inGame)
            controller.handleEvent(event);
        else
            Network.handleEvent(event);
        return event;
    }

    /**
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
     * Sends data back to {@link Session#client}.
     * 
     * @param buffer (ByteBuffer) data to send back.
     */
    public void sendToClient(ByteBuffer buffer) {
        client.send(buffer.array());
    }

    /**
     * Getter of {@link Session#player}.
     * 
     * @return (Player) player associated to this Session.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter of {@link Session#client}.
     * 
     * @return (GameClient) client associated to this Session.
     */
    public GameClient getClient() {
        return client;
    }
}
