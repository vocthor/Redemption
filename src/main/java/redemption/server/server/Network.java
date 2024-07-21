package redemption.server.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import lombok.extern.log4j.Log4j2;
import redemption.server.event.GameEvent;
import redemption.server.event.NetworkEvent;

/**
 * General purpose class to process Network events (connect, diconnect, etc) and
 * useful functions.
 */
@Log4j2
public class Network {
    /**
     * Size of received and sent buffers.
     */
    public static final int BUFFER_SIZE = 1024;

    /**
     * Endian used in the buffers. GameMaker Studio 2 uses Little Endian.
     */
    public static final ByteOrder ENDIAN = ByteOrder.LITTLE_ENDIAN;

    /**
     * Handle and process a {@link GameEvent}. This event must correspond to a
     * NetworkEvent.
     * TODO
     * 
     * @param event (GameEvent) event to handle and process.
     * @see {@link Session#getEvent(ByteBuffer)}.
     * @see {@link EventDecoder}.
     */
    public static void receiveEvent(NetworkEvent event) {
        log.info("New event received in Network. Processing it.");
        event.processEvent();
        // TODO
    }
}
