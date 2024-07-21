package redemption.server.event.gameevent;

import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import redemption.server.event.GameEvent;
import redemption.server.game.Actor;
import redemption.server.game.GameController;

@Log4j2
public class MoveEvent extends GameEvent {
    int _deltaX;
    int _deltaY;

    public MoveEvent() {
        super();
    }

    @Override
    public List<? extends Actor> processEvent(GameController controller) {
        log.info("Processing MoveEvent.");
        getPlayer().move(_deltaX, _deltaY);
        log.info("MoveEvent processed.");
        return Arrays.asList(getPlayer());
    }

    public void set_deltaX(int deltaX) {
        this._deltaX = deltaX;
    }

    public void set_deltaY(int deltaY) {
        this._deltaY = deltaY;
    }

}
