package redemption.server.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import redemption.server.game.GameController;

/**
 * General Server class. Accepts incoming connections, and creates a
 * {@link GameClient} thread for each connection.
 */
public class GameServer {
    /**
     * Port where the server is running
     */
    private int port;
    /**
     * Is the server running ?
     */
    private boolean running;
    /**
     * Socket associated
     */
    private ServerSocket socket;
    /**
     * List of clients connected to the server. A {@link GameClient} thread is
     * associated to each client.
     */
    private Set<GameClient> clients;
    /**
     * TODO
     */
    private GameController controller;

    public GameServer(int p) {
        clients = new HashSet<>();
        controller = new GameController(this);
        running = false;

        try {
            socket = new ServerSocket(p);
            port = p;
            running = true;
            // Affichage caracteristiques du serveur
            displayInfo();
        } catch (IOException e) {
            System.err.println("IOException error during creating the socket !");
            socket = null;
            running = false;
        }
    }

    /**
     * Main loop of the server. Accepts incoming connections, and creates a
     * {@link GameClient} thread for each connection.
     */
    public void run() {
        try {
            while (running) {
                // Tickrate du server ?
                Thread.sleep(1);
                // On accepte les connexions entrantes
                Socket s = socket.accept();
                System.out.println("A client has connected ! From : " + s.getInetAddress());
                // On créer un Thread par client pour les gérer
                GameClient c = new GameClient(this, s);
                c.setDaemon(true);
                clients.add(c);
                c.start();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Si pb on ferme le ServerSocket
            running = false;
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes a {@link GameClient} from {@link GameServer#clients}
     * @param c (GameClient) client to remove.
     */
    public void removeClient(GameClient c) {
        clients.remove(c);
    }

    /**
     * {@link GameServer#controller}
     * @return TODO
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Displays the information about this server : IPv4 address and port.
     * 
     */
    public void displayInfo() {
        System.out.println("Adresse : " + socket.getInetAddress() + " Port : " + port);
    }
}
