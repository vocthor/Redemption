package redemption.server.event.impl;

import java.util.Arrays;
import java.util.List;

import redemption.server.event.GameEvent;
import redemption.server.game.Actor;
import redemption.server.game.GameController;
import redemption.server.game.actors.Player;

public class DamageEvent extends GameEvent {

    private Player target;
    private int dmg;

    public DamageEvent(int t) {
        super(t);
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<Actor> processEvent(GameController controller) {
        target.takeDamage(dmg);
        System.out.println(target.getVie());
        return Arrays.asList(getPlayer(), target);
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

}
