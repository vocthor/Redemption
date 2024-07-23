package redemption.server.event.gameevent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;
import redemption.server.event.GameEvent;
import redemption.server.game.Actor;
import redemption.server.game.GameController;
import redemption.server.game.actors.Player;

@Log4j2
public class DamageEvent extends GameEvent {

    private UUID _targetUUID;
    private int _dmg;

    public DamageEvent() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<? extends Actor> processEvent(GameController controller) {
        log.info("Processing DamageEvent.");
        Player targetPlayer = controller.findPlayerByUUID(_targetUUID);
        if (targetPlayer == null) {
            log.warn("No valid target for damage ! The UUID doesn't match with any Player within the controller.");
            return new ArrayList<Player>();
        }
        targetPlayer.takeDamage(_dmg);
        System.out.println(targetPlayer.getHealth());
        log.info("DamageEvent processed.");
        return Arrays.asList(getPlayer(), targetPlayer);
    }

    public void setDmg(int dmg) {
        this._dmg = dmg;
    }

    public void setTargetUUID(UUID targetUUID) {
        this._targetUUID = targetUUID;
    }

}
