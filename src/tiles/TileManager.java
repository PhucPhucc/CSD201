package tiles;

import entity.Tree;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.Main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class TileManager {

    private GraphicsContext gc;
    private final Tiles[] tiles;
    private final int[][] mapTite;

    public ArrayList<Tree> trees;


    public TileManager(GraphicsContext gc) {
        this.gc = gc;
        tiles = new Tiles[30];
        mapTite = new int[Main.MAX_SCREEN_COL][Main.MAX_SCREEN_ROW];

        trees = new ArrayList<>();
        getTileImage();
        loadMap();
    }

    private void getTileImage() {
        try {

            tiles[0] = new Tiles();
            tiles[0].image =  new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/002.png")));

            // wall
            tiles[1] = new Tiles();
            tiles[1].image =  new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/032.png")));

            tiles[2] = new Tiles();
            tiles[2].image =  new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/019.png")));

            // tree
            tiles[3] = new Tiles();
            tiles[3].image =  new Image(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/016.png")));
            tiles[3].collision = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMap() {
        try {
            InputStream input = getClass().getResourceAsStream("../res/maps/map01.txt");
            assert input != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(input));

            int col = 0;
            int row = 0;

            while (col < Main.MAX_SCREEN_COL && row < Main.MAX_SCREEN_ROW) {
                String line = br.readLine();

                while (col < Main.MAX_SCREEN_COL) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTite[col][row] = num;
                    col++;
                }
                if (col == Main.MAX_SCREEN_COL) {
                    col = 0;
                    row++;
                }
            }


            br.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void drawMap() {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;


        while (col < Main.MAX_SCREEN_COL && row < Main.MAX_SCREEN_ROW) {
            int tileNum = mapTite[col][row];
            gc.drawImage(tiles[tileNum].image, x, y, Main.TITLE_SIZE, Main.TITLE_SIZE);
            col++;
            x += Main.TITLE_SIZE;
            if (col == Main.MAX_SCREEN_COL) {
                col = 0;
                x = 0;
                y += Main.TITLE_SIZE;
                row++;
            }
        }
    }

    private void createTrees(int level) {
        Tree tree;
        double rand = Math.random();
        if(rand < 0.2) {
            tree = new Tree(gc, true, level);
        } else {
            tree = new Tree(gc, false, level);
        }
        trees.add(tree);

    }

    int count = 200;
    int level = 0;
    int spawSpeed = 200;
    public void update() {
        Platform.runLater(() -> {
            count++;
            if (count > spawSpeed) {
                createTrees(level * (1 + level++));
                count = 0;
                spawSpeed -= level * 2;
                if(spawSpeed < 50) spawSpeed = 50;
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

            drawMap();
        for (Tree tree : trees) {
            tree.draw(gc);
        }
    }



}
