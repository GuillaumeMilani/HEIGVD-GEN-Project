package ch.heigvd.frogger;

import ch.heigvd.frogger.exception.CellAlreadyOccupiedException;
import ch.heigvd.frogger.item.Obstacle;
import ch.heigvd.frogger.item.Player;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class GameFXMLController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Create the canvas
            Canvas canvas = new Canvas(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
            Group elementsGroup = new Group();

            // Load the background
            Image background = new Image(
                    getClass().getResource(Constants.BACKGROUND_PATH).toString(),
                    Constants.GAME_WIDTH,
                    Constants.GAME_HEIGHT,
                    false, true);

            // Draw the background
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(background, 0, 0);

            // Skier on top of the mountain
            Player player = new Player(14, 5, Constants.ItemType.Skier);
            
            Group playerGroup = new Group();
            playerGroup.getChildren().add(player);
            
            // Create the two obstacles borders (chalets)
            for (int i = 0; i < Constants.NUM_ROWS; i++) {
                elementsGroup.getChildren().add(new Obstacle(0, i, Constants.ItemType.Chalet));
                elementsGroup.getChildren().add(new Obstacle(1, i, Constants.ItemType.Chalet));
                elementsGroup.getChildren().add(new Obstacle(Constants.NUM_COLS - 2, i, Constants.ItemType.ChaletVS));
                elementsGroup.getChildren().add(new Obstacle(Constants.NUM_COLS - 1, i, Constants.ItemType.ChaletVS));
            }

            // Create the static obstacles
            for (int i = 0; i < Constants.NUM_OBSTACLES; i++) {
                Random r = new Random();
                int x = 0;
                int y = 0;

                Obstacle sapin = new Obstacle(x, y, Constants.ItemType.Sapin); // sapin
                elementsGroup.getChildren().add(sapin);

                // TODO: Avoid infinite loop
                do {
                    sapin.setXGridCoordinate(r.nextInt(Constants.NUM_COLS - 4) + 2);
                    sapin.setYGridCoordinate(r.nextInt(Constants.NUM_ROWS - 2) + 2);
                } while (sapin.collisionWithOtherNode());
            }

            // ----- ONLY FOR DEBUG : draw lines -----
            // TODO : Remove
            // Draw the lines directly on the graphicContext
            gc.setStroke(Color.GRAY);
            gc.setLineWidth(1);
            for (int i = 0; i < Constants.NUM_ROWS; i++) {
                gc.strokeLine(0, i * Constants.CELL_HEIGHT, Constants.GAME_WIDTH, i * Constants.CELL_HEIGHT);
            }

            for (int i = 0; i < Constants.NUM_COLS; i++) {
                gc.strokeLine(i * Constants.CELL_WIDTH, 0, i * Constants.CELL_WIDTH, Constants.GAME_HEIGHT);
            }
            // ---------------------------------------

            // make the canvas focusable
            canvas.setFocusTraversable(true);

            AnchorPane.setTopAnchor(canvas, 0.);
            anchorPane.getChildren().add(canvas);
            anchorPane.getChildren().add(elementsGroup);
            anchorPane.getChildren().add(playerGroup);
                    
            // keyboard handler
            canvas.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (Constants.ACTION_ATTACK.containsKey(event.getCode())) {
                    System.out.println("Attacker's action : " + Constants.ACTION_ATTACK.get(event.getCode()) + " on " + event.getCode());
                    
                    try {
                        switch (event.getCode()) {
                            case LEFT:
                                player.setType(Constants.ItemType.SkierLeft);
                                player.moveLeft();
                                break;
                            case DOWN:
                                player.setType(Constants.ItemType.Skier);
                                player.moveBottom();
                                break;
                            case RIGHT:
                                player.setType(Constants.ItemType.SkierRight);
                                player.moveRight();
                                break;
                            default:
                                break;
                        }
                    } catch (CellAlreadyOccupiedException e) {
                        e.printStackTrace();
                    }
                } else if (Constants.ACTION_DEFEND.containsKey(event.getCode())) {
                    System.out.println("Defender's action : " + Constants.ACTION_DEFEND.get(event.getCode()) + " on " + event.getCode());
                }
            });
        } catch (CellAlreadyOccupiedException e2) {
            e2.printStackTrace();
        }
    }
}
