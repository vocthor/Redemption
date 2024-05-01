package redemption.server.event;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

import redemption.server.game.Actor;

/**
 * Class used to transform an event or a state into a buffer before sending it
 * back to the client.
 */
public class EventEncoder {

    /**
     * Adds the state of an actor the buffer.
     * 
     * @param buffer (ByteBuffer) buffer where to add the states.
     * @param actors ({@link Actor}) actor from whom to add the state in the buffer.
     * @return (int) the number of bytes added to the buffer.
     * @see {@link #addState(ByteBuffer, List)}.
     * @see {@link Actor#getState()}.
     */
    public static int addState(ByteBuffer buffer, Actor actor) {
        return addState(buffer, Arrays.asList(actor));
    }

    /**
     * Adds the state of each actors within the list into the buffer.
     * 
     * @param buffer (ByteBuffer) buffer where to add the states.
     * @param actors (List of {@link Actor}) actors from whom to add the state in
     *               the buffer.
     add* @return (int) the number of bytes added to the buffer.
     * @see {@link Actor#getState()}.
     */
    public static int addState(ByteBuffer buffer, Collection<? extends Actor> actors) {
        int pos = buffer.position();
        for (Actor a : actors) {
            ByteBuffer stateA = a.getState();
            int posA = stateA.position();
            buffer.put(stateA.array(), 0, posA); // byte[] because no need to look at the ByteBuffer
                                                 // position
        }
        return buffer.position() - pos;
    }

    public static void addSpecialEvent(ByteBuffer buffer) {

    }
}
