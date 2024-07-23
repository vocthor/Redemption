package redemption.server.game.actors;

import java.nio.ByteBuffer;

import redemption.server.game.Actor;
import redemption.server.server.Session;

/**
 * Represents a Actor played by a IRL player.
 * May be changed according to the game.
 * @see {@link Actor}.
 */
public class Player extends Actor {
    private int _health;
    private int _mana;
    private Session _session;

    public Player(Session s) {
        super();
        _session = s;
        _health = 100;
        _mana = 100;
    }

    public Session getSession() {
        return _session;
    }

    public int getHealth() {
        return _health;
    }

    public int getMana() {
        return _mana;
    }

    public void takeDamage(int dmg) {
        _health -= dmg;
    }

    public void move(int deltaX, int deltaY) {
        _positionX += deltaX;
        _positionY += deltaY;
        System.out.println("Position de " + _UUID + " X : " + _positionX + " & Y : " + _positionY);
    }

    @Override
    public ByteBuffer getState() {
        ByteBuffer buffer = super.getState();
        buffer.putInt(_health);
        buffer.putInt(_mana);
        return buffer;
    }
}
