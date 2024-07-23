package redemption.server.game;

import java.nio.ByteBuffer;
import java.util.UUID;

import redemption.server.utilities.Utilities;

/**
 * An Actor represents an entity within the game.
 * May be declined in {@link Player}, etc.
 * May be changed according to the game.
 */
public abstract class Actor {
    /**
     * {@link GameController} from where this Actor comes from.
     */
    protected GameController _controller;

    /**
     * Each Actor has an unique UUID, used to identify them.
     */
    protected UUID _UUID;

    protected int _positionX;
    protected int _positionY;

    public Actor() {
        _UUID = java.util.UUID.randomUUID();
    }

    /**
     * Getter of {@link #_controller}.
     * 
     * @return (GameController) controller associated to this Actor.
     */
    public GameController getController() {
        return _controller;
    }

    /**
     * Setter of {@link #_controller}.
     * 
     * @param controller (GameController) controller associated to this Actor.
     */
    public void setController(GameController controller) {
        this._controller = controller;
    }

    /**
     * Getter of {@link #_UUID}.
     * 
     * @return (UUID) uuid associated to this Actor.
     */
    public UUID getUUID() {
        return _UUID;
    }

    /**
     * Setter of {@link #_UUID}.
     * 
     * @param UUID (UUID) uuid to associate to this Actor.
     */
    public void setUUID(UUID UUID) {
        this._UUID = UUID;
    }

    /**
     * THIS FUNCTIONS MUST BE OVERRIDDEN AND CALLED WITHIN CHILD CLASSES.
     * 
     * @return (ByteBuffer) new buffer with UUID, class name, and both positions
     *         already put.
     */
    public ByteBuffer getState() {
        ByteBuffer buffer = Utilities.newBuffer();
        // On met l'UUID dans le buffer sous forme de 2 long
        Utilities.putUUID(buffer, _UUID);
        // On met la class de l'objet (qui extends Actor)
        String srcClass = this.getClass().getSimpleName();
        Utilities.putStringGMS2(buffer, srcClass);
        // On met les coordonnées
        buffer.putInt(_positionX);
        buffer.putInt(_positionY);
        return buffer;
    }
}
