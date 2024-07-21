package redemption.server.event.networkevent;

import lombok.extern.log4j.Log4j2;
import redemption.server.event.NetworkEvent;
import redemption.server.server.GameServer;

@Log4j2
public class StartGameEvent extends NetworkEvent {
    
    @Override
    public void processEvent() {
        log.info("Processing StartGameEvent.");
        GameServer server = GameServer.getInstance();
        server.getGameController().start();
    }

}
