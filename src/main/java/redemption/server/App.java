package redemption.server;

import redemption.server.server.GameServer;

/**
 * Main function, entry point of the program.
 */
public class App {
    public static void main(String[] args) {
        GameServer s1 = new GameServer(5555);
        s1.run();
    }
}
