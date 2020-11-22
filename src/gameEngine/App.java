package gameEngine;

import gui.MainPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

/*
Divyansh's:
TODO: Add go back option, load game page
TODO: Beware of file changes and refactoring, especially in file paths while making jar
TODO: Add effects for gameplay
TODO: Add game buttons on pause game screen
TODO: Better game over screen, link it with gameplay, and have boolean for end in GamePlay
*/

public class App extends Application {

    private MediaPlayer mediaPlayer;
    private Scene scene;

    private void addAssets() {
        Media bgMusic = new Media(new File("src/assets/music/bg2.mp3").toURI().toString());
        this.mediaPlayer = new MediaPlayer(bgMusic);

        this.mediaPlayer.setAutoPlay(true);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.mediaPlayer.play();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.addAssets();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainPage.fxml")); // keep in mind, referencing of loaders & objects
        Parent root = loader.load();
        MainPageController mainPageController = loader.getController();
        mainPageController.init(this);

        StackPane rootContainer = new StackPane(root); // roots of this stack pane will be switched
        rootContainer.setStyle("-fx-background-color :  #03060F;"); // for pixel based positioning issues
        primaryStage.initStyle(StageStyle.UNDECORATED);
        this.scene = new Scene(rootContainer); // scene's root is the rootContainer (stackPane) whose root is our switching panes
        this.scene.setCursor(new ImageCursor(new Image(new File("src/assets/mainPage/cursor.png").toURI().toString())));
        primaryStage.setScene(this.scene);
        primaryStage.show();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        new App();
        launch(args);
    }
}
