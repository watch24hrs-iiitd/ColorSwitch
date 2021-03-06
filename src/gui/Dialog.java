package gui;

import gameEngine.GamePlay;
import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

// for popups like confirmations and stuff
public class Dialog { // TODO location of stage

    public Dialog(String msg, Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dialog.fxml"));
        Parent dialogRoot = null;
        try {
            dialogRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load dialog root");
            e.printStackTrace();
        }
        assert (dialogRoot != null);

        Label msgLabel = (Label) dialogRoot.getChildrenUnmodifiable().get(1);
        msgLabel.setText(msg);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(dialogRoot);
        scene.setFill(Color.TRANSPARENT);
        Stage secondaryStage = new Stage(StageStyle.TRANSPARENT);
        secondaryStage.initModality(Modality.NONE);
        secondaryStage.initOwner(primaryStage);
        secondaryStage.setScene(scene);
        secondaryStage.show();

        System.out.println(this.getClass().toString() + " focused secondaryStage before: " + secondaryStage.isFocused());
        dialogRoot.setFocusTraversable(false);
        primaryStage.requestFocus();
        System.out.println(this.getClass().toString() + " focused secondaryStage after: " + secondaryStage.isFocused());
        System.out.println(this.getClass().toString() + " focused prim stage: " + primaryStage.isFocused());

        secondaryStage.setX((primScreenBounds.getWidth() - secondaryStage.getWidth()) / 2);
        secondaryStage.setY(GamePlay.HEIGHT + secondaryStage.getHeight() / 3.25 + 1);
        System.out.println(this.getClass().toString() + " secondary stage: " + secondaryStage.getX() + ", " + secondaryStage.getY());

        dialogRoot.setOpacity(0.0);
        FadeTransition exitAnim = new FadeTransition(Duration.seconds(0.75), dialogRoot);
        exitAnim.setToValue(0.0);
        exitAnim.setOnFinished(t -> secondaryStage.close());
        PauseTransition delay = new PauseTransition(Duration.seconds(1.25));
        delay.setOnFinished(t -> exitAnim.play());
        FadeTransition showAnim = new FadeTransition(Duration.seconds(0.75), dialogRoot);
        showAnim.setToValue(1);
        showAnim.setOnFinished(t -> delay.play());
        showAnim.play();
    }
}
