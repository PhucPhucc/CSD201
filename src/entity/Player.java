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

public class Player extends Entity {

    private GraphicsContext gc;
    private final AnchorPane root;

    private Image[] img;
    private int ground;
    private Image image;

    boolean isSpace = false;

    public Player(GraphicsContext gc, AnchorPane root) {
        this.gc = gc;
        this.root = root;
        img = new Image[6];

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = Main.TITLE_SIZE;
        y = Main.TITLE_SIZE * 5;
        ground = Main.TITLE_SIZE * 5;
        speed = 6;
        gravity = 7;
    }

    public void getPlayerImage() {
        try {

            img[0] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/cat1.png")));
            img[1] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/cat2.png")));
            img[2] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/cat3.png")));
            img[3] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/cat4.png")));
            img[4] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/cat_jump1.png")));
            img[5] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/cat_jump2.png")));

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


            root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode().toString().equals("SPACE")) {
                    isSpace = true;
                }
            });

            if (isSpace && !isJumping && !isFalling) {
                isJumping = true;
            }

            if (isJumping) {
                if (y > 200) {
                    y -= speed;
                    image = img[4];

                } else {
                    isJumping = false;
                    isFalling = true;
                }
            }

            if (isFalling) {
                if (y < ground) {
                    y += gravity;
                    image = img[5];
                } else {
                    isFalling = false;
                }
            }

            isSpace = false;
        });
    }
    int count = 0;
    int num = 0;
    public void draw(GraphicsContext gc) {
        count++;
        if (!isJumping && !isFalling && count > 10) {
            image = img[num];
            num++;
            if(num == 4) num = 0;
            count = 0;
        }
        gc.drawImage(image, x, y, Main.TITLE_SIZE, Main.TITLE_SIZE);

    }


}
