package redemption.server.event.networkevent;

import lombok.extern.log4j.Log4j2;
import redemption.server.event.NetworkEvent;
import redemption.server.server.GameServer;

@Log4j2
public class ConnectGameEvent extends NetworkEvent {

    @Override
    public void processEvent() {
        log.info("Processing ConnectGameEvent.");
        GameServer server = GameServer.getInstance();
        try {
            session.connectToGame(server.getGameController());
        } catch (Exception e) {
            log.error("Could not connect Session to the GameController.");
        }
    }

}
