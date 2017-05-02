package ch.heigvd.frogger;

import ch.heigvd.frogger.exception.CellAlreadyOccupiedException;
import ch.heigvd.frogger.item.Item;
import java.util.Arrays;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Guillaume Milani
 * @date 28/04/17
 */
public class Grid {

    private final Item[][] items;
    private static Grid grid = null;

    final double cellWidth = (double)Constants.GAME_WIDTH / (double)Constants.NUM_COLS;
    final double cellHeight = (double)Constants.GAME_HEIGHT / (double)Constants.NUM_ROWS;

    public static Grid getInstance() {
        if (grid == null) {
            Grid.grid = new Grid();
        }
        return grid;
    }

    private Grid() {
        this.items = new Item[Constants.NUM_COLS][Constants.NUM_ROWS];
        for (Item[] item : items) {
            Arrays.fill(item, null);
        }
    }

    public Item getItem(int x, int y) throws IllegalArgumentException {
        checkIndex(x, y);
        return items[x][y];
    }

    public void addItem(Item item) throws CellAlreadyOccupiedException {
    	addItem(item, item.getPosX(), item.getPosY());
    }

    public void addItem(Item item, int x, int y) throws CellAlreadyOccupiedException {
        checkIndex(x, y);
        if (items[x][y] != null) {
        	System.out.println(x+", "+y+" : "+items[x][y]);
            throw new CellAlreadyOccupiedException();
        }
        items[x][y] = item;
    }

    public Item removeItem(int x, int y) throws IllegalArgumentException {
        checkIndex(x, y);

        if (items[x][y] == null) {
            throw new IllegalArgumentException("Can't remove element from empty cell");
        }
        Item item = items[x][y];
        items[x][y] = null;
        return item;

    }

    public double getCellHeight() {
        return cellHeight;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public boolean isFree(int x, int y) throws IllegalArgumentException {
        checkIndex(x, y);
        return items[x][y] == null;
    }

    public void draw(Canvas canvas) {
    	// Draw the lines directly on the graphicContext
    	GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        for (int i = 0; i < Constants.NUM_ROWS; i++) {
            gc.strokeLine(0, i * cellHeight, Constants.GAME_WIDTH, i * cellHeight);
        }

        for (int i = 0; i < Constants.NUM_COLS; i++) {
            gc.strokeLine(i * cellWidth, 0, i * cellWidth, Constants.GAME_HEIGHT);
        }

        for (int x = 0; x < items.length; x++) {
            for (int y = 0; y < items[x].length; y++) {
                if (items[x][y] != null) {
                    items[x][y].draw(canvas);
                }
            }
        }
    }

    private void checkIndex(int x, int y) throws IllegalArgumentException {
        if (x < 0 || x >= Constants.NUM_COLS || y < 0 || y >= Constants.NUM_ROWS) {
            throw new IllegalArgumentException("Incorrect element index");
        }
    }

}
