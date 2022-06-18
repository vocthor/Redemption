package redemption.server.event;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import redemption.server.event.impl.DamageEvent;
import redemption.server.event.impl.MoveEvent;

/**
 * Class used to transform data into the corresponding event.
 * 
 * @see {@link EventType}.
 */
public class EventDecoder {
    /**
     * Type of the event.
     * 
     * @see {@link GameEvent#type}.
     * @see {@link EventType}.
     */
    private static int type;

    /**
     * Handle data to create the corresponding {@link GameEvent}.
     * 
     * @param buffer (ByteBuffer) data to handle.
     * @return (GameEvent) event newly created corresponding to the data.
     */
    public static GameEvent handle(ByteBuffer buffer) {
        switch (type = buffer.get()) {
            case EventType.PLAYER_MOVE:
                return handlePlayerMove(buffer);
            case EventType.ATTACK:
                return handleAttack(buffer);
            case EventType.PLAYER_SPELL1:
                return handleSpell1(buffer);
            case EventType.PLAYER_SPELL2:
                return handleSpell2(buffer);
            case EventType.Z:
                return handleZ(buffer);
            case EventType.Q:
                return handleQ(buffer);
            case EventType.S:
                return handleS(buffer);
            case EventType.D:
                return handleD(buffer);
            default:
                return handlePlayerMove(buffer);
            // throw new IllegalArgumentException();
        }
    }

    private static GameEvent handlePlayerMove(ByteBuffer buffer) {
        byte[] arr = new byte[buffer.remaining()];
        buffer.get(arr);
        System.out.println(new String(arr, StandardCharsets.UTF_8));
        return null;
    }

    private static GameEvent handleAttack(ByteBuffer buffer) {
        DamageEvent damageEvent = new DamageEvent(type);
        damageEvent.setDmg(buffer.get());
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
        MoveEvent moveEvent = new MoveEvent(type);
        moveEvent.setDeltaY(-10);
        return moveEvent;
    }

    private static GameEvent handleQ(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent(type);
        moveEvent.setDeltaX(-10);
        return moveEvent;
    }

    private static GameEvent handleS(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent(type);
        moveEvent.setDeltaY(10);
        return moveEvent;
    }

    private static GameEvent handleD(ByteBuffer buffer) {
        MoveEvent moveEvent = new MoveEvent(type);
        moveEvent.setDeltaX(10);
        return moveEvent;
    }

}
