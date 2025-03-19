package main;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class HomeController {

    @FXML
    private Pane aboutUsPane;

    @FXML
    private ListView<Integer> listRank;

    @FXML
    private AnchorPane paneRanking;

    @FXML
    private void closeModal() {
        aboutUsPane.setVisible(false);
        paneRanking.setVisible(false);
    }

    @FXML
    private void openAboutUs() {
        aboutUsPane.setVisible(true);
    }

    @FXML
    private void openRanking() {
        paneRanking.setVisible(true);
        readScore();
        System.out.println(scores.toString());
        ObservableList<Integer> dataList = FXCollections.observableArrayList(scores);
        listRank.setItems(dataList);
        listRank.setCellFactory(list -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer score, boolean empty) {
                super.updateItem(score, empty);

                if (empty || score == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    int rank = getIndex() + 1; // Tính thứ hạng (bắt đầu từ 1)

                    Text text = new Text(rank + " - " + score + " điểm");
                    text.setFont(Font.font("Arial", 16));

                    // Tô màu tùy theo thứ hạng
                    if (rank == 1) {
                        text.setFill(Color.GOLD);
                    } else if (rank == 2) {
                        text.setFill(Color.SILVER);
                    } else if (rank == 3) {
                        text.setFill(Color.BROWN);
                    } else {
                        text.setFill(Color.BLACK);
                    }

                    setGraphic(text);
                }
            }
        });


    }

    @FXML
    private void openHelps() {
        aboutUsPane.setVisible(true);
    }

    @FXML
    void exitGame() {

    }

    @FXML
    private void switchToGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/res/FXML/game.fxml")));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT));
        stage.setTitle("Game Scene");
    }

    private ArrayList<Integer> scores = new ArrayList<>();


    private void readScore() {
        scores.clear();

        try (InputStream inputStream = getClass().getResourceAsStream("/res/score/ranking.txt")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Không tìm thấy file ranking.txt");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(Integer.parseInt(line.trim()));
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Không thể đọc file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Lỗi định dạng trong file: " + e.getMessage());
        }
    }

}
