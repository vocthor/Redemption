package redemption.server.event.gameevent;

import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import redemption.server.event.GameEvent;
import redemption.server.game.Actor;
import redemption.server.game.GameController;

@Log4j2
public class MoveEvent extends GameEvent {
    private int _deltaX;
    private int _deltaY;

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

    public void setDeltaX(int deltaX) {
        this._deltaX = deltaX;
    }

    public void setDeltaY(int deltaY) {
        this._deltaY = deltaY;
    }

}
