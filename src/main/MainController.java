
package main;

import entity.Player;
import entity.Tree;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import tiles.TileManager;

import javax.imageio.ImageIO;

import javafx.scene.shape.Rectangle;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;


public class MainController implements Runnable {

    @FXML
    private Canvas gameCanvas;

    @FXML
    private AnchorPane root;

    @FXML
    private Label scoreLabel;

    @FXML
    private Pane paneGameOver;

    private GraphicsContext gc;

    private final int FPS = 60;
    private int score = 0;
    private boolean isRunning = true;
    private ArrayList<Integer> scores;
    String filePath = Objects.requireNonNull(getClass().getResource("/res/score/ranking.txt")).getPath();

    Image img;
    TileManager tileManager;
    Player player;
    Thread gameThread;

    public MainController() throws IOException {
        initialize();


    }

    private void startGameThread() {
//        img = new Image(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("../res/player/boy_right_1.png")).toExternalForm()));

        gameThread = new Thread(this);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public void initialize() {
        if (gameCanvas == null) {
            return;
        }

        gc = gameCanvas.getGraphicsContext2D();
        try {
            BufferedImage bi = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_right_1.png")));

            img = SwingFXUtils.toFXImage(bi, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assert root != null;
        player = new Player(gc, root);
        tileManager = new TileManager(gc);
        scores = new ArrayList<>();
        readScore();


        startGameThread(); // Chỉ gọi sau khi gameCanvas đã được gán
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;


        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                draw();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                timer = 0;
                drawCount = 0;
            }


        }
    }

    private void update() {
        if (isRunning) {
            score++;
            tileManager.update();
            player.update();

            if (!tileManager.trees.isEmpty()) { // Chỉ lấy nếu có cây
                Platform.runLater(() -> {
                    Tree tree = tileManager.trees.get(0);

                    int playerWidth = 28;  // Giảm chiều rộng (nếu quá lớn)
                    int playerHeight = 35; // Giảm chiều cao (nếu bị lệch)
                    int offsetX = 22;      // Dịch hitbox về trung tâm hơn
                    int offsetY = 20;

                    Rectangle playerBox = new Rectangle(player.x + offsetX, player.y + offsetY, playerWidth, playerHeight);

                    int treeWidth = tree.getIsBigger() ? 60 : 25;  // Điều chỉnh theo kích thước cây
                    int treeHeight = 40;  // Có thể giảm để không tính phần gốc cây
                    int treeOffsetX = tree.getIsBigger() ? 24 : 20; // Dịch hitbox về trung tâm
                    int treeOffsetY = 12; // Dịch hitbox về trung tâm

                    Rectangle treeBox = new Rectangle(tree.x + treeOffsetX, tree.y + treeOffsetY, treeWidth, treeHeight);

//                    playerBox.setStroke(Color.RED);
//                    playerBox.setFill(Color.TRANSPARENT);
//                    playerBox.setStrokeWidth(2);
//
//                    treeBox.setStroke(Color.BLUE);
//                    treeBox.setFill(Color.TRANSPARENT);
//                    treeBox.setStrokeWidth(2);
//
//                    playerBox.toFront();
//                    treeBox.toFront();
//
//                    // Xóa các Rectangle cũ nếu cần (tránh tạo quá nhiều object)
//                    root.getChildren().removeIf(node -> node instanceof Rectangle);
//
//                    // Thêm vào AnchorPane
//                    root.getChildren().addAll(playerBox, treeBox);

                    if (playerBox.intersects(treeBox.getBoundsInLocal())) {
                        gameOver();
                    }

                });
            }
        } else {
            paneGameOver.setVisible(true);
        }
    }

    private void draw() {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

            tileManager.draw(gc);
            player.draw(gc);
            scoreLabel.setText("Điểm số của bạn: " + score);
        });
    }

    private void gameOver() {
        isRunning = false;
        System.out.println(score);
        writeScore();
    }

    @FXML
    private void resetGame() {
        paneGameOver.setVisible(false);
        score = 0;
        tileManager.trees.clear();
        isRunning = true;
        tileManager = new TileManager(gc);
        readScore();
    }

    @FXML
    private void backMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/res/FXML/home.fxml")));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT));
        stage.setTitle("Home Scene");
    }


    private void readScore() {
        scores.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(Integer.parseInt(line.trim()));
            }
        } catch (IOException e) {
            System.out.println("Không thể đọc file: " + e.getMessage());
        }

    }

    private void writeScore() {
        boolean isAdd = true;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (int s : scores) {
                if(score > s && isAdd) {
                    writer.write(score + "\n");
                    isAdd = false;
                }
                writer.write(s + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }
}
