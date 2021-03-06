package gui;

import gameEngine.App;
import gameEngine.GamePlay;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainPageController {

    private App app;
    private HashMap<Group, Circle> iconCircleMap;
    private HashMap<Group, ImageView> iconImgMap;
    private AudioClip hoverSound;
    private AudioClip clickSound;
    private AudioClip exitSound;

    private Image playImgHoverActive;
    private Image playImgHoverInactive;

    // for modes
    private HashMap<Group, Group> otherMode;
    private Group activeMode;

    // for glow on non classic mode
    private Timeline bubbleBgGlow;

    @FXML
    private AnchorPane mainPageRoot;
    @FXML
    private Group iconLB;
    @FXML
    private Group iconLoad;
    @FXML
    private Group iconExit;
    @FXML
    private Group iconModes1; // classic
    @FXML
    private Group iconModes2; // other mode
    @FXML
    private Circle circleLB;
    @FXML
    private Circle circleLoad;
    @FXML
    private Circle circleExit;
    @FXML
    private ImageView imgLB;
    @FXML
    private ImageView imgLoad;
    @FXML
    private ImageView imgExit;
    @FXML
    private ImageView outerPlayRing;
    @FXML
    private ImageView innerPlayRing;
    @FXML
    private ImageView logoRingL;
    @FXML
    private ImageView logoRingR;
    @FXML
    private ImageView bg;
    @FXML
    private ImageView bubbleBg;

    @SuppressWarnings("unused")
    @FXML
    private Group iconSettings;


    public void init(App _app) { // note, init always before scene transitions, might utilize data member, leads to exceptions
        this.initData(_app);
        this.initAnim();
    }

    public void initData(App _app) { // for init data members
        this.app = _app; // need for database and stuff too

        this.iconCircleMap = new HashMap<>();
        this.iconCircleMap.put(iconLB, circleLB);
        this.iconCircleMap.put(iconLoad, circleLoad);
        this.iconCircleMap.put(iconExit, circleExit);

        this.playImgHoverActive = new Image(new File("src/assets/mainPage/playDamped.png").toURI().toString());
        this.playImgHoverInactive = new Image(new File("src/assets/mainPage/play.png").toURI().toString());

        this.iconImgMap = new HashMap<>();
        this.iconImgMap.put(iconLB, imgLB);
        this.iconImgMap.put(iconLoad, imgLoad);
        this.iconImgMap.put(iconExit, imgExit);

        // create anim timer for bubbleBg
        Glow g = (Glow) this.bubbleBg.getEffect();
        this.bubbleBgGlow = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(g.levelProperty(), g.getLevel(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(1.25), new KeyValue(g.levelProperty(), 1, Interpolator.EASE_BOTH))
        );
        bubbleBgGlow.setAutoReverse(true);
        this.bubbleBgGlow.setCycleCount(Animation.INDEFINITE);

        // set modes
        this.otherMode = new HashMap<>();
        this.otherMode.put(this.iconModes1, this.iconModes2);
        this.otherMode.put(this.iconModes2, this.iconModes1);
        if (GamePlay.IS_CLASSIC) {
            this.activeMode = this.iconModes1;
            this.deactivateMode(this.iconModes2);
        } else {
            this.activeMode = this.iconModes2;
            this.deactivateMode(this.iconModes1);
        }

        this.hoverSound = new AudioClip(new File("src/assets/music/mouse/hover.wav").toURI().toString());
        this.clickSound = new AudioClip(new File("src/assets/music/mouse/button.wav").toURI().toString());
        this.exitSound = new AudioClip(new File("src/assets/music/exit2.mp3").toURI().toString());
        this.clickSound.setVolume(0.5);
        this.hoverSound.setVolume(0.04);
    }

    public void initAnim() { // for starting anim

        if (!GamePlay.IS_CLASSIC) {
            this.displayOtherModeEffects();
        }

        RotateTransition rtIn = new RotateTransition(Duration.millis(15000), innerPlayRing);
        RotateTransition rtOut = new RotateTransition(Duration.millis(15000), outerPlayRing);
        RotateTransition rtLogoRingL = new RotateTransition(Duration.millis(15000), logoRingL);
        RotateTransition rtLogoRingR = new RotateTransition(Duration.millis(15000), logoRingR);
//        RotateTransition rtIconLB = new RotateTransition(Duration.millis(15000), iconLB);
//        RotateTransition rtIconLoad = new RotateTransition(Duration.millis(15000), iconLoad);
//        RotateTransition rtIconExit = new RotateTransition(Duration.millis(15000), iconExit);
        this.startRotation(rtIn, 1);
        this.startRotation(rtOut, -1);
        this.startRotation(rtLogoRingL, 1);
        this.startRotation(rtLogoRingR, 1);
//        this.startRotation(rtIconLB, 1);
//        this.startRotation(rtIconLoad, 1);
//        this.startRotation(rtIconExit, 1);
    }

    private void startRotation(RotateTransition rt, int dir) {
        rt.setByAngle(dir * 720);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }

    private void deactivateMode(Group group) { // for switching between modes
        group.setOpacity(0.7);
    }

    @FXML
    public void modeHoverActive(MouseEvent mouseEvent) {
        Group g = (Group) mouseEvent.getSource();
        if (this.activeMode == g) return;
        this.hoverSound.play();
        this.otherMode.get(this.activeMode).setOpacity(0.85);
    }

    @FXML
    public void modeHoverInactive(MouseEvent mouseEvent) {
        Group g = (Group) mouseEvent.getSource();
        if (this.activeMode == g) return;
        this.otherMode.get(this.activeMode).setOpacity(0.7);
    }

    @FXML
    public void modeSwitchClicked(MouseEvent mouseEvent) {
        Group g = (Group) mouseEvent.getSource();
        if (this.activeMode == g) return;
        this.clickSound.play();

        this.activeMode = g;
        this.activeMode.setOpacity(1);
        deactivateMode(this.otherMode.get(this.activeMode));

        // switch mode, already existing player can't ever come on main page
        GamePlay.IS_CLASSIC = !GamePlay.IS_CLASSIC;
        System.out.println(this.getClass().toString() + "mode switched, is classic mode: " + GamePlay.IS_CLASSIC);

        if (this.activeMode == this.iconModes2) { // currently switched to non classic mode
            this.displayOtherModeEffects();
        } else {
            this.restoreNormalModeEffects();
        }
    }

    private void restoreNormalModeEffects() {
        Timeline rmBg = new Timeline(
                new KeyFrame(Duration.seconds(1), new KeyValue(this.bubbleBg.opacityProperty(), 0, Interpolator.EASE_IN), new KeyValue(this.bg.opacityProperty(), 0.6, Interpolator.EASE_IN))
        );
        rmBg.setOnFinished(t -> this.bubbleBgGlow.stop());
        rmBg.play();
    }

    private void displayOtherModeEffects() {
        // add extra effects on main screen for non classic mode
        Glow g = (Glow) this.bubbleBg.getEffect(); // reset glow to 0, might be that previous anim was interrupted
        g.setLevel(0);
        Timeline showBg = new Timeline(
          new KeyFrame(Duration.seconds(1), new KeyValue(this.bubbleBg.opacityProperty(), 0.2, Interpolator.EASE_IN), new KeyValue(this.bg.opacityProperty(), 0.3, Interpolator.EASE_IN))
        );
        showBg.setOnFinished(t -> this.bubbleBgGlow.play());
        showBg.play();
    }

    @FXML
    public void playHoverActive(MouseEvent mouseEvent) {
        this.hoverSound.play();
        ImageView source = (ImageView) mouseEvent.getSource();
        source.setImage(this.playImgHoverActive);
    }

    @FXML
    public void playHoverInactive(MouseEvent mouseEvent) {
        ImageView source = (ImageView) mouseEvent.getSource();
        source.setImage(this.playImgHoverInactive);
    }

    @FXML
    public void iconHoverActive(MouseEvent mouseEvent) {
        Group group = (Group) mouseEvent.getSource();
        Circle circle = iconCircleMap.get(group);
        ImageView img = iconImgMap.get(group);

        this.hoverSound.play();
        circle.setFill(Color.web("#FFB52E"));
        circle.setScaleX(1.1);
        circle.setScaleY(1.1);
        img.setScaleX(1.1);
        img.setScaleY(1.1);
    }

    @FXML
    public void iconHoverInactive(MouseEvent mouseEvent) {
        Group group = (Group) mouseEvent.getSource();
        Circle circle = iconCircleMap.get(group);
        ImageView img = iconImgMap.get(group);

        circle.setFill(Color.YELLOW);
        circle.setScaleX(1);
        circle.setScaleY(1);
        img.setScaleX(1);
        img.setScaleY(1);
    }

    @FXML
    public void exitPressed() throws InterruptedException {
        App.BgMediaPlayer.stop();
        this.exitSound.play();
        System.out.println("Game ded :(");
        Thread.sleep(500);
        System.exit(0);
    }

    @FXML
    public void playPressed() {
        this.clickSound.play();
        Scene scene = this.app.getScene();
        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 1);
        rootContainer.getChildren().remove(mainPageRoot);
        try {
            new GamePlay(this.app); // app automatically gives a scene reference
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " New game failed to load!");
            e.printStackTrace();
        }
    }

    @FXML
    public void loadGamePressed() {
        this.clickSound.play();
        Scene scene = this.app.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadGamePage.fxml"));
        Parent loadGameRoot = null;
        try {
            loadGameRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load loadGameRoot");
            e.printStackTrace();
        }
        assert (loadGameRoot != null);
        LoadGamePageController loadGamePageController = loader.getController(); // init the controller
        loadGamePageController.init(this.app); // for which load slots are present
        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 1);
        rootContainer.getChildren().add(loadGameRoot);

        // temp rectangle for fade purpose
        Rectangle fadeR = new Rectangle();
        fadeR.setWidth(GamePlay.WIDTH);
        fadeR.setHeight(GamePlay.HEIGHT);
        fadeR.setFill(Paint.valueOf("#000000"));
        fadeR.setOpacity(0);
        rootContainer.getChildren().add(fadeR);

        loadGameRoot.translateXProperty().set(GamePlay.WIDTH);
        Timeline timelineSlide = new Timeline();
        KeyValue kvSlide = new KeyValue(loadGameRoot.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfSlide = new KeyFrame(Duration.seconds(1), kvSlide);
        timelineSlide.getKeyFrames().add(kfSlide);
        timelineSlide.setOnFinished(t -> rootContainer.getChildren().remove(this.mainPageRoot));

        Timeline timelineFadeOut = new Timeline();
        KeyValue kvFadeOut = new KeyValue(fadeR.opacityProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfFadeOut = new KeyFrame(Duration.seconds(0.5), kvFadeOut);
        timelineFadeOut.getKeyFrames().add(kfFadeOut);
        timelineFadeOut.setOnFinished(t -> rootContainer.getChildren().remove(fadeR));

        Timeline timelineFadeIn = new Timeline();
        KeyValue kvFadeIn = new KeyValue(fadeR.opacityProperty(), 0.75, Interpolator.EASE_IN);
        KeyFrame kfFadeIn = new KeyFrame(Duration.seconds(1), kvFadeIn);
        timelineFadeIn.getKeyFrames().add(kfFadeIn);
        timelineFadeIn.setOnFinished(t -> timelineFadeOut.play());

        timelineSlide.play();
        timelineFadeIn.play();
    }

    @FXML
    public void lBPressed() {
        this.clickSound.play();
        Scene scene = this.app.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LBPage.fxml"));
        Parent lBRoot = null;
        try {
            lBRoot = loader.load();
        } catch (IOException e) {
            System.out.println(this.getClass().toString() + " failed to load lBRoot");
            e.printStackTrace();
        }
        assert (lBRoot != null);
        LBPageController lbPageController = loader.getController();
        lbPageController.init(this.app, null, true); // as no game is initiated on main page
        StackPane rootContainer = (StackPane) scene.getRoot();
        assert (rootContainer.getChildren().size() == 1);
        rootContainer.getChildren().add(lBRoot);

        // temp rectangle for fade purpose
        Rectangle fadeR = new Rectangle();
        fadeR.setWidth(GamePlay.WIDTH);
        fadeR.setHeight(GamePlay.HEIGHT);
        fadeR.setFill(Paint.valueOf("#000000"));
        fadeR.setOpacity(0);
        rootContainer.getChildren().add(fadeR);

        lBRoot.translateXProperty().set(-GamePlay.WIDTH);
        Timeline timelineSlide = new Timeline();
        KeyValue kvSlide = new KeyValue(lBRoot.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfSlide = new KeyFrame(Duration.seconds(1), kvSlide);
        timelineSlide.getKeyFrames().add(kfSlide);
        timelineSlide.setOnFinished(t -> rootContainer.getChildren().remove(this.mainPageRoot));

        Timeline timelineFadeOut = new Timeline();
        KeyValue kvFadeOut = new KeyValue(fadeR.opacityProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kfFadeOut = new KeyFrame(Duration.seconds(0.5), kvFadeOut);
        timelineFadeOut.getKeyFrames().add(kfFadeOut);
        timelineFadeOut.setOnFinished(t -> rootContainer.getChildren().remove(fadeR));

        Timeline timelineFadeIn = new Timeline();
        KeyValue kvFadeIn = new KeyValue(fadeR.opacityProperty(), 0.75, Interpolator.EASE_IN);
        KeyFrame kfFadeIn = new KeyFrame(Duration.seconds(1), kvFadeIn);
        timelineFadeIn.getKeyFrames().add(kfFadeIn);
        timelineFadeIn.setOnFinished(t -> timelineFadeOut.play());

        timelineSlide.play();
        timelineFadeIn.play();
    }
}