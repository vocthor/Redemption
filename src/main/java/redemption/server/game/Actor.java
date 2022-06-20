package redemption.server.game;

import java.nio.ByteBuffer;
import java.util.UUID;

import redemption.server.server.Network;

/**
 * An Actor represents an entity within the game.
 * May be declined in {@link Player}, etc.
 * May be changed according to the game.
 */
public abstract class Actor {
    /**
     * {@link GameController} from where this Actor comes from.
     */
    protected GameController controller;

    /**
     * Each Actor has an unique UUID, used to identify them.
     */
    protected UUID UUID;

    protected int positionX;
    protected int positionY;

    public Actor() {
        UUID = java.util.UUID.randomUUID();
    }

    /**
     * Getter of {@link Actor#controller}.
     * 
     * @return (GameController) controller associated to this Actor.
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Setter of {@link Actor#controller}.
     * 
     * @param controller (GameController) controller associated to this Actor.
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * THIS FUNCTIONS MUST BE OVERRIDDEN AND CALLED WITHIN CHILD CLASSES.
     * 
     * @return (ByteBuffer) new buffer with UUID, class name, and both positions
     *         already put.
     */
    public ByteBuffer getState() {
        ByteBuffer buffer = ByteBuffer.allocate(Network.BUFFER_SIZE);
        // On met l'UUID dans le buffer sous forme de 2 long
        buffer.putLong(UUID.getMostSignificantBits());
        buffer.putLong(UUID.getLeastSignificantBits());
        // On met la class de l'objet (qui extends Actor)
        String srcClass = this.getClass().getSimpleName();
        Network.putStringGMS2(buffer, srcClass);
        // On met les coordonnées
        buffer.putInt(positionX);
        buffer.putInt(positionY);
        return buffer;
    }
}
