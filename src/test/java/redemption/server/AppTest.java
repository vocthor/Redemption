package redemption.server;

import static org.junit.Assert.assertTrue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.junit.Test;

import redemption.server.server.Network;

class TestClient {
    private Socket socket;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    public TestClient(String ip, int port) throws Exception {
        socket = new Socket(ip, port);
        dataIn = new DataInputStream(socket.getInputStream());
        dataOut = new DataOutputStream(socket.getOutputStream());

        Thread receiveLoop = new Thread() {
            public void run() {
                while (true) {
                    try {
                        byte buffer[] = new byte[Network.BUFFER_SIZE];
                        int nbByte = dataIn.read(buffer);
                        if (nbByte > 0) {
                            System.err.println("Paquet recu : " + buffer);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveLoop.setDaemon(true);
        receiveLoop.start();

    }

    public void send(byte[] buffer) {
        try {
            dataOut.write(buffer);
            dataOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopConnection() throws Exception {
        dataIn.close();
        dataOut.close();
        socket.close();
    }

}

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Test à la mano pour l'instant
     */

    public static void main(String[] args) throws Exception {
        TestClient c1 = new TestClient("0.0.0.0", 5555);
        c1.send(new byte [] {0x01});
        Thread.sleep(1000);
        c1.send(new byte [] {0x02});
        Thread.sleep(1000);
        c1.send(new byte [] {0x61, 0x15});
    }
}