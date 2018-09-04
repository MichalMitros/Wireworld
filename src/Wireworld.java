import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Wireworld extends Application {

    private Scene scene;
    private GridPane pane;
    private Board board;
    private ImageView wireworldLogoImage;
    private Button playButton;
    private Button clearButton;
    private Button saveButton;
    private Button openButton;
    private Timeline timer;
    private Timeline drawTimer;
    private Slider delaySlider;
    private boolean play = false;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Wireworld");

        pane = new GridPane();
        pane.setHgap(10);
        pane.setBackground(new Background(new BackgroundFill(Color.gray(0.5), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setAlignment(Pos.CENTER);

        wireworldLogoImage = new ImageView(new Image("logo.png"));
        pane.add(wireworldLogoImage, 0, 0);

        board = new Board();
        board.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if(e.getButton() == MouseButton.PRIMARY) {
                board.applyClick((int)e.getX(), (int)e.getY(), false);
            } else if(e.getButton() == MouseButton.SECONDARY) {
                board.applyClick((int)e.getX(), (int)e.getY(), true);
            }

        });
        board.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if(e.getButton() == MouseButton.PRIMARY) {
                board.applyDrag((int)e.getX(), (int)e.getY(), false);
            } else if(e.getButton() == MouseButton.SECONDARY) {
                board.applyDrag((int)e.getX(), (int)e.getY(), true);
            }

        });
        pane.add(board, 0, 1, 6, 1);

        drawTimer = new Timeline(new KeyFrame(Duration.millis(75), e -> {
            board.drawBoard();
        }));
        drawTimer.setCycleCount(Animation.INDEFINITE);
        drawTimer.play();

        timer = new Timeline(new KeyFrame(Duration.millis(250), e -> {
            board.nextGeneration();
        }));
        timer.setCycleCount(Animation.INDEFINITE);

        playButton = new Button("Play");
        playButton.setMinWidth(80);
        playButton.setMinHeight(35);
        playButton.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.SPACE) {
                playPause();
            }
        });
        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> playPause());
        pane.add(playButton, 5, 0);

        clearButton = new Button("Clear");
        clearButton.setMinWidth(70);
        clearButton.setMinHeight(35);
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            board.clear();
            play = false;
            timer.stop();
            playButton.setText("Play");
        });
        pane.add(clearButton, 4, 0);

        saveButton = new Button("Save");
        saveButton.setMinWidth(70);
        saveButton.setMinHeight(35);
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            play = false;
            timer.stop();
            playButton.setText("Play");
            BoardSaver.saveToFile(primaryStage, board);
        });
        pane.add(saveButton, 1, 0);

        openButton = new Button("Open");
        openButton.setMinWidth(70);
        openButton.setMinHeight(35);
        openButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            play = false;
            timer.stop();
            playButton.setText("Play");
            try {
                board.applyNewBoard(BoardLoader.loadFromFile(primaryStage));
            } catch(IOException ioe) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot open board from file.");

                alert.showAndWait();
            }
        });
        pane.add(openButton, 2, 0);

        delaySlider = new Slider();
        delaySlider.setMin(40);
        delaySlider.setMax(1500);
        delaySlider.setValue(250);
        delaySlider.setShowTickLabels(true);
        delaySlider.setShowTickMarks(true);
        delaySlider.setMajorTickUnit(960);
        delaySlider.setMinorTickCount(20);
        delaySlider.setBlockIncrement(20);
        delaySlider.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            timer.stop();
            timer = new Timeline(new KeyFrame(Duration.millis((int)delaySlider.getValue()), e2 -> {
                board.nextGeneration();
                board.drawBoard();
            }));
            timer.setCycleCount(Animation.INDEFINITE);
            if(play) {
                timer.play();
            }
        });
        pane.add(delaySlider, 3, 0);

        scene = new Scene(pane, 600, 640);
        scene.setFill(Color.gray(0.5));
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.SPACE) {
                playPause();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void playPause() {
        if(play) {
            play = false;
            timer.stop();
            playButton.setText("Play");
        } else {
            play = true;
            timer.play();
            playButton.setText("Stop");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
