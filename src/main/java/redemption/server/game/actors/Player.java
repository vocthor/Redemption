package redemption.server.game.actors;

import java.nio.ByteBuffer;

import redemption.server.game.Actor;
import redemption.server.server.Network;
import redemption.server.server.Session;

/**
 * Represents a Actor played by a IRL player.
 * May be changed according to the game.
 * @see {@link Actor}.
 */
public class Player extends Actor {
    private int vie;
    private int mana;
    private Session session;

    public Player(Session s) {
        super();
        session = s;
        vie = 100;
        mana = 100;
    }

    public Session getSession() {
        return session;
    }

    public int getVie() {
        return vie;
    }

    public int getMana() {
        return mana;
    }

    public void takeDamage(int dmg) {
        vie -= dmg;
    }

    public void move(int deltaX, int deltaY) {
        positionX += deltaX;
        positionY += deltaY;
        System.out.println("Position de " + UUID + " X : " + positionX + " & Y : " + positionY);
    }

    @Override
    public ByteBuffer getState() {
        ByteBuffer buffer = super.getState();
        buffer.putInt(vie);
        buffer.putInt(mana);
        return buffer;
    }
}
