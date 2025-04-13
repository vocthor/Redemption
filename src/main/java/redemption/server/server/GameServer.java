package redemption.server.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j2;
import redemption.server.game.GameController;

/**
 * General Server class. Accepts incoming connections, and creates a
 * {@link GameClient} thread for each connection.
 */
@Log4j2
public class GameServer {

    private static volatile GameServer instance;

    public static GameServer getInstance() {
        GameServer res = instance;
        if (res != null)
            return res;
        synchronized (GameServer.class) {
            if (instance == null)
                instance = new GameServer();
            return instance;
        }
    }

    /**
     * Port where the server is running
     */
    private int _port;
    /**
     * Is the server running ?
     */
    private boolean _running;
    /**
     * Socket associated
     */
    private ServerSocket _socket;
    /**
     * List of clients connected to the server. A {@link GameClient} thread is
     * associated to each client.
     */
    private Set<GameClient> _clients;
    /**
     * TODO
     */
    private GameController _gameController;

    private GameServer() {
        _clients = new HashSet<>();
        _gameController = new GameController();
        _gameController.setDaemon(true);

        _running = false;
        try {
            int p = Integer.valueOf(System.getenv("SERVER_PORT"));
            _socket = new ServerSocket(p);
            _port = p;
            _running = true;
            // Affichage caracteristiques du serveur
            log.info("Server started on port " + _port);
        } catch (IOException e) {
            log.error("IOException error during creating the socket !", e);
            _socket = null;
            _running = false;
        }
    }

    /**
     * Main loop of the server. Accepts incoming connections, and creates a
     * {@link GameClient} thread for each connection.
     */
    public void run() {
        try {
            while (_running) {
                // Tickrate du server ?
                Thread.sleep(1);
                // On accepte les connexions entrantes
                Socket s = _socket.accept();
                log.info("A client has connected from : " + s.getInetAddress());
                // On crée un Thread par client pour les gérer
                GameClient c = new GameClient(s);
                c.setDaemon(true);
                _clients.add(c);
                c.start();
            }
        } catch (IOException | InterruptedException e) {
            log.error("The Server encountered an error !", e);
        } finally {
            // Si pb on ferme le ServerSocket
            log.info("Stopping server.");
            _running = false;
            try {
                _socket.close();
            } catch (IOException e) {
                log.error("The server encountered an error while closing !",e);
            }
        }
    }

    /**
     * Removes a {@link GameClient} from {@link GameServer#clients}
     * 
     * @param c (GameClient) client to remove.
     */
    public void removeClient(GameClient c) {
        _clients.remove(c);
    }

    /**
     * {@link GameServer#_gameController}
     * 
     * @return TODO
     */
    public GameController getGameController() {
        return _gameController;
    }
}
