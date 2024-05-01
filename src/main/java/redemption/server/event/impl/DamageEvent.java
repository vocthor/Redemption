package redemption.server.event.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import redemption.server.event.GameEvent;
import redemption.server.game.Actor;
import redemption.server.game.GameController;
import redemption.server.game.actors.Player;

public class DamageEvent extends GameEvent {

    private UUID targetUUID;
    private int dmg;

    public DamageEvent() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<? extends Actor> processEvent(GameController controller) {
        Player targetPlayer = controller.findPlayerByUUID(targetUUID);
        if (targetPlayer == null) {
            System.err.println("No Valid Target ! The UUID doesn't match with any Player within the controller.");
            return new ArrayList<Player>();
        }
        targetPlayer.takeDamage(dmg);
        System.out.println(targetPlayer.getVie());
        return Arrays.asList(getPlayer(), targetPlayer);
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public void setTargetUUID(UUID targetUUID) {
        this.targetUUID = targetUUID;
    }

}
