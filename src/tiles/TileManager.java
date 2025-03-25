package tiles;

import entity.Tree;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import main.Main;

import java.util.ArrayList;
import java.util.Iterator;

public class TileManager {

    private final GraphicsContext gc;

    public ArrayList<Tree> trees;


    public TileManager(GraphicsContext gc) {
        this.gc = gc;
        trees = new ArrayList<>();
    }


    private void createTrees(int level) {
        Tree tree;
        double rand = Math.random();
        if (rand < 0.3) {
            tree = new Tree(gc, true, level);
        } else {
            tree = new Tree(gc, false, level);
        }
        trees.add(tree);

    }

    int count = 150;
    int level = 0;
    int spawnSpeed = 150;

    public void update() {
        Platform.runLater(() -> {
            count++;
            if (count > spawnSpeed) {
                createTrees(level++);
                count = 0;
                if (spawnSpeed > 100) {
                    spawnSpeed -= (int) Math.pow(level, 1.2);
                }
            }

            Iterator<Tree> iterator = trees.iterator();
            while (iterator.hasNext()) {
                Tree tree = iterator.next();
                double treeX = tree.update();
                if (treeX < -(Main.TITLE_SIZE * 2)) {
                    iterator.remove();
                }
            }
        });
    }


    public void draw(GraphicsContext gc) {
        for (Tree tree : trees) {
            tree.draw(gc);
        }
    }


}
