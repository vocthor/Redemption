package redemption.server.utilities;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Utilities {

    /**
     * GameMaker Studio 2 uses a special character to end Strings (just like C).
     */
    public static final byte GMS2_ENDING = 0x00;

    /**
     * Puts a String within a ByteBuffer. Be careful, it also puts an int just
     * before the String, corresponding to the String length.
     * 
     * @param buffer (ByteBuffer) buffer where to put the String.
     * @param string (String) String to add to the ByteBuffer.
     */
    public static void putString(ByteBuffer buffer, String string) {
        int strLength = string.length();
        buffer.putInt(strLength);
        byte[] strB = string.getBytes(StandardCharsets.UTF_8);
        buffer.put(strB);
    }

    /**
     * Puts a String within a ByteBuffer. Be careful, it also puts an int just
     * before the String, corresponding to the String length.
     * The String will be under the GameMaker format, ending with 0x00.
     * 
     * @param buffer (ByteBuffer) buffer where to put the String.
     * @param string (String) String to add to the ByteBuffer.
     */
    public static void putStringGMS2(ByteBuffer buffer, String string) {
        putString(buffer, string);
        buffer.put(GMS2_ENDING);
    }

    /**
     * Reads a String from a ByteBuffer. Be careful, it needs an int just before the
     * String, specifing the String length.
     * 
     * @param buffer (ByteBuffer) from where to read the String.
     * @return (String) the String read.
     */
    public static String getString(ByteBuffer buffer) {
        int strLength = buffer.getInt();
        byte[] strB = new byte[strLength];
        buffer.get(strB, 0, strLength);
        return new String(strB, StandardCharsets.UTF_8);
    }

    /**
     * Reads a String from a ByteBuffer. Be careful, it needs an int just before the
     * String, specifing the String length.
     * The String must be under the GameMaker format, ending with 0x00.
     * 
     * @param buffer (ByteBuffer) from where to read the String.
     * @return (String) the String read.
     * @throws IllegalArgumentException if the String does not end with 0x00.
     */
    public static String getStringGMS2(ByteBuffer buffer) {
        String res = getString(buffer);
        if (buffer.get() != GMS2_ENDING) {
            throw new IllegalArgumentException();
        }
        return res;
    }
    
    /**
     * 
     * @param buffer
     * @param uuid
     */
    public static void putUUID(ByteBuffer buffer, UUID uuid) {
        // On met l'UUID dans le buffer sous forme de 2 long
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
    }

    /**
     * 
     * @param buffer
     * @return
     */
    public static UUID getUUID(ByteBuffer buffer) {
        long mostUUID = buffer.getLong();
        long lessUUID = buffer.getLong();
        return new UUID(mostUUID, lessUUID);
    }

}
