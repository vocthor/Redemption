package redemption.server.event;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import redemption.server.event.impl.DamageEvent;
import redemption.server.event.impl.MoveEvent;
import redemption.server.utilities.Utilities;

/**
 * Class used to transform data into the corresponding event.
 * 
 * @see {@link EventType}.
 */
public class EventDecoder {

    private static final Map<Byte, Function<ByteBuffer, Event>> eventTypeMap = new HashMap<>() {
        {
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
        return eventTypeMap.getOrDefault(buffer.get(), EventDecoder::handlePlayerMove).apply(buffer);
    }

    private static GameEvent handlePlayerMove(ByteBuffer buffer) {
        byte[] arr = new byte[buffer.remaining()];
        buffer.get(arr);
        System.out.println(new String(arr, StandardCharsets.UTF_8));
        return null;
    }

    private static GameEvent handleAttack(ByteBuffer buffer) {
        DamageEvent damageEvent = new DamageEvent();
        damageEvent.setTargetUUID(Utilities.getUUID(buffer));
        damageEvent.setDmg(buffer.getInt());
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
        moveEvent.setDeltaY(-10);
        return moveEvent;
    }

    private static GameEvent handleQ(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent();
        moveEvent.setDeltaX(-10);
        return moveEvent;
    }

    private static GameEvent handleS(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent();
        moveEvent.setDeltaY(10);
        return moveEvent;
    }

    private static GameEvent handleD(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent();
        moveEvent.setDeltaX(10);
        return moveEvent;
    }

}
