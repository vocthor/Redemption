package redemption.server.event.impl;

import java.util.Arrays;
import java.util.List;

import redemption.server.event.GameEvent;
import redemption.server.game.Actor;
import redemption.server.game.GameController;

public class MoveEvent extends GameEvent {
    int deltaX;
    int deltaY;

    public MoveEvent() {
        super();
    }

    @Override
    public List<? extends Actor> processEvent(GameController controller) {
        getPlayer().move(deltaX, deltaY);
        return Arrays.asList(getPlayer());
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

}
