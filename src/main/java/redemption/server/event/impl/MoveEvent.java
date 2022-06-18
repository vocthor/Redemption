package redemption.server.event.impl;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import redemption.server.event.GameEvent;
import redemption.server.game.Actor;
import redemption.server.game.GameController;

public class MoveEvent extends GameEvent {
    int deltaX;
    int deltaY;

    public MoveEvent(int t) {
        super(t);
    }

    @Override
    public List<Actor> processEvent(GameController controller) {
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
