package redemption.server.utilities;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import redemption.server.server.Network;

public class Utilities {

    /**
     * GameMaker Studio 2 uses a special character to end Strings (just like C).
     */
    public static final byte GMS2_ENDING = 0x00;

    /**
     * Creates a new ByteBuffer with a size of {@link Network#BUFFER_SIZE} bytes.
     * This buffer uses {@link Network#ENDIAN} endianness. This buffer is empty with the
     * position at 0.
     * 
     * @return the newly created ByteBuffer.
     */
    public static ByteBuffer newBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(Network.BUFFER_SIZE);
        buffer.order(Network.ENDIAN);
        return buffer;
    }

    /**
     * Creates a new ByteBuffer with a size of {@link Network#BUFFER_SIZE} bytes.
     * This buffer uses {@link Network#ENDIAN} endianness. This buffer is not empty,
     * containing the byte array parameter.
     * 
     * @param buf (byte []) array of bytes to convert into a ByteBuffer.
     * @return the newly created ByteBuffer.
     */
    public static ByteBuffer newBuffer(byte[] buf) {
        ByteBuffer buffer = ByteBuffer.allocate(Network.BUFFER_SIZE);
        buffer.order(Network.ENDIAN);
        buffer.put(buf);
        buffer.position(0);
        return buffer;
    }

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
     * Puts an {@link UUID} into a ByteBuffer. The most significant bits are first,
     * then comes the least significant ones.
     * 
     * @param buffer (ByteBuffer) buffer where to put the UUID.
     * @param uuid   (UUID) UUID to add to the ByteBuffer.
     */
    public static void putUUID(ByteBuffer buffer, UUID uuid) {
        // On met l'UUID dans le buffer sous forme de 2 long
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
    }

    /**
     * Reads an UUID from a ByteBuffer. Be careful, the most significant bits are
     * read first, then the least significant ones.
     * 
     * @param buffer (ByteBuffer) from where to read the UUID.
     * @return (UUID) the UUID read.
     */
    public static UUID getUUID(ByteBuffer buffer) {
        long mostUUID = buffer.getLong();
        long lessUUID = buffer.getLong();
        return new UUID(mostUUID, lessUUID);
    }

}
