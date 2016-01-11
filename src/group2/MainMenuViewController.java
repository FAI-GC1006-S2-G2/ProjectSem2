package group2;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;

/**
 * Author: Gác Xanh (phamanh)
 * Date: 11/01/2016
 * Class: OOP2
 * Project: ProjectSem2
 */
public class MainMenuViewController extends Application {
    private Canvas canvas;
    private Button playButton;
    private Button exitButton;

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ISIS");
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        canvas = new Canvas(Config.WindowProperties.WINDOW_WIDTH, Config.WindowProperties.WINDOW_HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image backgroundImage2 = new Image(new FileInputStream("menus/Load.png"));
        gc.drawImage(backgroundImage2, 0, 0, canvas.getWidth(), canvas.getHeight());

        playButton = new Button();
        setupPlayButton();
        exitButton = new Button();
        setupExitButton();
        playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                GameViewController gameViewController = new GameViewController();
                try {

                    primaryStage.close();
                    gameViewController.start(primaryStage);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });

        root.getChildren().add(playButton);
        root.getChildren().add(exitButton);
        primaryStage.show();
    }

    public MainMenuViewController() {
    }

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }

    private void setupPlayButton() {

        playButton.setFont(Font.font("Chalkduster", FontWeight.BOLD, 40));

        playButton.setText("PLAY");
        playButton.setPrefSize(180, 40);
        playButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        playButton.setLayoutX(canvas.getWidth() - 250);
        playButton.setLayoutY(canvas.getHeight() - 350);
        playButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                try {
                    playButton.setTextFill(Color.BLACK);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        playButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                try {
//                    SoundManager.playSound("sounds/button.wav");
                    playButton.setTextFill(Color.RED);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
    }

    private void setupExitButton() {

        exitButton.setFont(Font.font("Chalkduster", FontWeight.BOLD, 40));

        exitButton.setText("EXIT");
        exitButton.setPrefSize(180, 40);


        exitButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        exitButton.setLayoutX(canvas.getWidth() - 250);
        exitButton.setLayoutY(canvas.getHeight() - 270);
        exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                try {
                    System.exit(0);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        exitButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                try {
                    exitButton.setTextFill(Color.BLACK);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        exitButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                try {
//                    SoundManager.playSound("sounds/button.wav");
                    exitButton.setTextFill(Color.RED);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
    }
}
