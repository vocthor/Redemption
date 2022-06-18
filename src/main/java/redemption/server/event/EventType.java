package redemption.server.event;

/**
 * Class used to store the byte values used to transform data into a event.
 * Each constant is encoded onto 8 bits.
 * 
 * @see {@link EventDecoder}.
 */
public class EventType {
    public final static byte PLAYER_SPELL1 = 0x62;
    public final static byte PLAYER_SPELL2 = 0x63;
    public final static byte PLAYER_MOVE = 0x04;
    public final static byte ATTACK = 0x61;
    public final static byte Z = 0x7A;
    public final static byte Q = 0x71;
    public final static byte S = 0x73;
    public final static byte D = 0x64;

}
