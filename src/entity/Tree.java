package entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.Main;

import java.util.Objects;

public class Tree extends Entity{

    private GraphicsContext gc;
    private Image img;
    private boolean isBigger;

    public Tree(GraphicsContext gc, boolean isBigger, int level) {
        this.gc = gc;
        this.isBigger = isBigger;
        setDefaultValue();
        speed = 5 +  (double) (level);;
    }

    private void setDefaultValue() {
        x = Main.TITLE_SIZE * 16;
        y = Main.TITLE_SIZE * 5;
        try {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../res/tiles/016.png")));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public double update() {
        x -= speed;
        return x;
    }

    public void draw(GraphicsContext gc) {
        if (isBigger) {
            gc.drawImage(img, x, y, Main.TITLE_SIZE, Main.TITLE_SIZE);
            gc.drawImage(img, x + Main.TITLE_SIZE, y, Main.TITLE_SIZE, Main.TITLE_SIZE);
        } else {
            gc.drawImage(img, x, y, Main.TITLE_SIZE, Main.TITLE_SIZE);
        }
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public boolean getIsBigger() {
        return isBigger;
    }

}
