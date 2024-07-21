package redemption.server.event;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import lombok.extern.log4j.Log4j2;
import redemption.server.event.gameevent.DamageEvent;
import redemption.server.event.gameevent.MoveEvent;
import redemption.server.event.networkevent.ConnectGameEvent;
import redemption.server.event.networkevent.StartGameEvent;
import redemption.server.server.GameServer;
import redemption.server.utilities.Utilities;

/**
 * Class used to transform data into the corresponding event.
 * 
 * @see {@link EventType}.
 */
@Log4j2
public class EventDecoder {

    private static final Map<Byte, Function<ByteBuffer, Event>> EVENT_TYPE_MAP = new HashMap<>() {
        {
            // NETWORK
            put(EventType.START_GAME, EventDecoder::handleStartGame);
            put(EventType.CONNECT_GAME, EventDecoder::handleConnectGame);

            // GAME
            put(EventType.PLAYER_MOVE, EventDecoder::handlePlayerMove);
            put(EventType.ATTACK, EventDecoder::handleAttack);
            put(EventType.PLAYER_SPELL1, EventDecoder::handleSpell1);
            put(EventType.PLAYER_SPELL1, EventDecoder::handleSpell1);
            put(EventType.Z, EventDecoder::handleZ);
            put(EventType.Q, EventDecoder::handleQ);
            put(EventType.S, EventDecoder::handleS);
            put(EventType.D, EventDecoder::handleD);
        }
    };

    /**
     * Handle data to create the corresponding {@link GameEvent}.
     * 
     * @param buffer (ByteBuffer) data to handle.
     * @return (GameEvent) event newly created corresponding to the data.
     */
    public static Event decode(ByteBuffer buffer) {
        log.info("Decoding Event from data.");
        return EVENT_TYPE_MAP.getOrDefault(buffer.get(), EventDecoder::unrecognizedEvent).apply(buffer);
    }

    /**
     * Dummy function for when a buffer could not be interpreted
     * 
     * @param buffer (ByteBuffer) buffer to handle
     * @return (Event) null
     */
    private static Event unrecognizedEvent(ByteBuffer buffer) {
        System.out.println("This buffer cannot be parsed correctly !");
        System.out.println(Arrays.toString(buffer.array()));
        return null;
    }

    private static GameEvent handlePlayerMove(ByteBuffer buffer) {
        byte[] arr = new byte[buffer.remaining()];
        buffer.get(arr);
        System.out.println(new String(arr, StandardCharsets.UTF_8));
        return null;
    }

    private static GameEvent handleAttack(ByteBuffer buffer) {
        DamageEvent damageEvent = new DamageEvent();
        damageEvent.set_targetUUID(Utilities.getUUID(buffer));
        damageEvent.set_dmg(buffer.getInt());
        System.out.println("Attack");
        return damageEvent;
    }

    private static GameEvent handleSpell1(ByteBuffer buffer) {
        throw new UnsupportedOperationException("handleSpell1 not implemented");
    }

    private static GameEvent handleSpell2(ByteBuffer buffer) {
        throw new UnsupportedOperationException("handleSpell2 not implemented");

    }

    private static GameEvent handleZ(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent();
        moveEvent.set_deltaY(-10);
        return moveEvent;
    }

    private static GameEvent handleQ(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent();
        moveEvent.set_deltaX(-10);
        return moveEvent;
    }

    private static GameEvent handleS(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent();
        moveEvent.set_deltaY(10);
        return moveEvent;
    }

    private static GameEvent handleD(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent();
        moveEvent.set_deltaX(10);
        return moveEvent;
    }

    private static NetworkEvent handleStartGame(ByteBuffer buffer) {
        return new StartGameEvent();
    }

    private static NetworkEvent handleConnectGame(ByteBuffer buffer) {
        return new ConnectGameEvent();
    }
}
