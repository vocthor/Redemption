package redemption.server.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import lombok.extern.log4j.Log4j2;

/**
 * This a Thread used to collect data send by one player.
 * Each Client correspond to 1 player, and each player will have 1 Client.
 * Watch out ! This is NOT the actual client-side (which will be implemented in
 * GameMaker)
 */
@Log4j2
public class GameClient extends Thread {
    /**
     * Is this client connected ?
     */
    private boolean _connected;
    /**
     * Socket associated to the client connection
     */
    private Socket _socket;
    /**
     * {@link GameServer} from where this controller comes from.
     */
    private GameServer _server;
    /**
     * Input Stream used to receive data send by the client.
     */
    private DataInputStream _dataIn; // Is DataInputStream the correct class to use instead of just InputStream or
    // BufferedInputStream or ByteArrayInputStream ?
    /**
     * Ouput Stream used to send data send to the client.
     */
    private DataOutputStream _dataOut;
    /**
     * {@link Session} associated to this client.
     */
    private Session _session;

    public GameClient(Socket s) {
        super();
        log.info("Creating new GameClient thread.");
        _server = GameServer.getInstance();
        _connected = true;
        try {
            // Ouverture Input et Output
            _dataIn = new DataInputStream(s.getInputStream());
            _dataOut = new DataOutputStream(s.getOutputStream());
            _socket = s;
        } catch (IOException e) {
            log.error("GameClient could not initiate input and output streams !", e);
        }
        _session = new Session(this);
    }

    /**
     * Main loop of the Thread. While the client is connected, we wait for data to
     * come, and then we send it to the {@link GameClient#_session} to process it.
     * Data is received and send under a byte[].
     * 
     * @see {@link Session#getEvent(byte[])}.
     */
    @Override
    public void run() {
        try {
            log.info("Starting GameClient listen loop.");
            while (_connected) {
                byte buffer[] = new byte[Network.BUFFER_SIZE];
                int nbByte = _dataIn.read(buffer);
                if (nbByte > 0) {
                    log.info("Data received. Passing it down to Session.");
                    // On yeet gameEvent a la session
                    _session.getEvent(buffer);
                }
            }
        } catch (IOException e) {
            log.error("Error when receiving data from client ! ", e);
        } finally {
            log.info("Stopping client Thread.");
            _connected = false;
            _server.removeClient(this);
            // Si pb on ferme le Socket
            try {
                _session.close();
                _socket.close();
            } catch (Exception e) {
                log.error("The client couldn't be correctly disconnected !", e);
            }
        }
    }

    /**
     * Sends data to the client corresponding to this Thread.
     * 
     * @param buffer (byte[]) data to send.
     */
    public void send(byte[] buffer) {
        log.info("Sending data to client.");
        try {
            _dataOut.write(buffer);
            _dataOut.flush();
        } catch (IOException e) {
            log.warn("Data could not be correctly sent to client", e);
        }
    }
}
