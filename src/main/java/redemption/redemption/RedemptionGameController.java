package redemption.redemption;

import java.util.NoSuchElementException;

import lombok.Getter;
import lombok.Setter;
import redemption.server.game.Actor;
import redemption.server.game.GameController;

public class RedemptionGameController extends GameController {
    private Board board;

    public RedemptionGameController() {
        super();
        board = new Board();
    }

    public Actor getActorByPosition(double positionX, double positionY, double positionZ)
            throws NoSuchElementException {
        int pX = (int) positionX;
        int pY = (int) positionY;
        if (!board.tiles[pX][pY].isOccupied())
            throw new NoSuchElementException("Tile empty.");
        return board.tiles[pX][pY].getActor();
    }

}

/**
 * Board
 */
class Board {
    final int BOARD_HEIGHT = 10;
    final int BOARD_WIDTH = 10;
    Tile[][] tiles;

    /**
     * Tile
     */
    @Setter
    @Getter
    public class Tile {
        private Actor actor;

        // TODO USELESS ?
        public boolean isOccupied() {
            return actor != null;
        }
    }

    public Board() {
        tiles = new Tile[BOARD_HEIGHT][BOARD_WIDTH];
    }
}