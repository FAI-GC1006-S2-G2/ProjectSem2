/**
 * Created by BTC on 2015/12/17.
 */

package group2;

import group2.Scene.GameScene;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameViewController extends Application {

    // Properties
    private GameScene gameScene;
    private Button backButton;

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ISIS");
        gameScene = new GameScene();
        backButton = new Button();
        backButton.setFont(Font.font("Chalkduster", FontWeight.BOLD, 18));
        backButton.setText("BACK");
        backButton.setPrefSize(100, 20);
        backButton.setLayoutX(Config.WindowProperties.WINDOW_WIDTH - backButton.getPrefWidth() - 5);
        backButton.setLayoutY(20);
        backButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        gameScene.root.getChildren().add(backButton);
        backButton.setFocusTraversable(false);
        backButton.setOnMouseExited(arg0 -> {
            try {
                backButton.setTextFill(Color.BLACK);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        backButton.setOnMouseEntered(arg0 -> {
            try {
                backButton.setTextFill(Color.WHITE);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        backButton.setOnMouseClicked(arg0 -> {
            MainMenuViewController mainMenuViewController = new MainMenuViewController();
            try {
                gameScene.backToMainMenu();
                primaryStage.close();
                mainMenuViewController.start(primaryStage);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

}
