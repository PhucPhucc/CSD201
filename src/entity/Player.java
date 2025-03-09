package entity;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import main.Main;
import sun.plugin.javascript.navig.Anchor;

import java.util.Objects;

public class Player extends Entity{

    private GraphicsContext gc;
    private final AnchorPane root;

    private Image img;
    private int ground;

    boolean isSpace = false;
    public Player(GraphicsContext gc, AnchorPane root) {
        this.gc = gc;
        this.root = root;


        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {
        x = Main.TITLE_SIZE;
        y = Main.TITLE_SIZE * 5;
        ground = Main.TITLE_SIZE * 5;
        speed = 5;
    }

    public void getPlayerImage() {
        try {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../res/player/boy_right_1.png")));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    boolean isJumping = false;
    boolean isFalling = false;
    public void update() {
        Platform.runLater(() -> {
            root.setFocusTraversable(true);
            root.requestFocus();

        });

        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().toString().equals("SPACE")) {
                isSpace = true;
            }
        });

        if (isSpace && !isJumping && !isFalling) {
            isJumping = true;
        }

        if (isJumping) {
            if (y > 175) {
                y -= speed;
            } else {
                isJumping = false;
                isFalling = true;
            }
        }

        if (isFalling) {
            if (y < ground) {
                y += speed;
            } else {
                isFalling = false;
            }
        }
        isSpace = false;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(img, x, y, Main.TITLE_SIZE, Main.TITLE_SIZE);
    }


}
