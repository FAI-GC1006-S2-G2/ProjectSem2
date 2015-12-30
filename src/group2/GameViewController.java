package group2;

import group2.Scene.GameScene;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by BTC on 2015/12/17.
 */
public class GameViewController extends Application {

    // Properties
    GameScene gameScene;

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ISIS");
        gameScene = new GameScene();
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

}
