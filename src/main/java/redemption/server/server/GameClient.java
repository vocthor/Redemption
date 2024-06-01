package redemption.server.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This a Thread used to collect data send by one player.
 * Each Client correspond to 1 player, and each player will have 1 Client.
 * Watch out ! This is NOT the actual client-side (which will be implemented in
 * GameMaker)
 */
public class GameClient extends Thread {
    /**
     * Is this client connected ?
     */
    private boolean connected;
    /**
     * Socket associated to the client connection
     */
    private Socket socket;
    /**
     * {@link GameServer} from where this controller comes from.
     */
    private GameServer server;
    /**
     * Input Stream used to receive data send by the client.
     */
    private DataInputStream dataIn; // Is DataInputStream the correct class to use instead of just InputStream or
                            // BufferedInputStream or ByteArrayInputStream ?
    /**
     * Ouput Stream used to send data send to the client.
     */
    private DataOutputStream dataOut;
    /**
     * {@link Session} associated to this client.
     */
    private Session session;

    public GameClient(GameServer serv, Socket s) {
        super();
        server = serv;
        connected = true;
        try {
            // Ouverture Input et Output
            dataIn = new DataInputStream(s.getInputStream());
            dataOut = new DataOutputStream(s.getOutputStream());
            socket = s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        session = new Session(this);
        // TEMPORAIRE
        try {
            session.connectToGame(server.getGameController());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Main loop of the Thread. While the client is connected, we wait for data to
     * come, and then we send it to the {@link GameClient#session} to process it.
     * Data is received and send under a byte[].
     * @see {@link Session#getEvent(byte[])}.
     */
    @Override
    public void run() {
        try {
            while (connected) {
                byte buffer[] = new byte[Network.BUFFER_SIZE];
                int nbByte = dataIn.read(buffer);
                if (nbByte > 0) {
                    // On yeet gameEvent a la session
                    session.getEvent(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connected = false;
            server.removeClient(this);
            System.out.println("A client has disconnected !");
            // Si pb on ferme le Socket
            try {
                socket.close();
                session.close();
            } catch (IOException e1) {
                System.err.println("The client socket couldn't be closed !");
                e1.printStackTrace();
            }
        }
    }

    /**
     * Sends data to the client corresponding to this Thread.
     * @param buffer (byte[]) data to send.
     */
    public void send(byte[] buffer) {
        try {
            dataOut.write(buffer);
            dataOut.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
