package gameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;

public class HighScoreLine extends GameElement {

    private static final Image image = new Image(new File("src/assets/gameplay/crown_s.png").toURI().toString());

    HighScoreLine(double y) {
        super(0, y, 0);
    }
    @Override
    public void refresh(GraphicsContext graphicsContext){
        graphicsContext.drawImage(image, 5, y - image.getHeight()/1.5);
        Renderer.drawDashedLine(image.getWidth() + 13, y, 2, 10, 5, Color.WHITE);
    }
    @Override
    public boolean checkCollision(Ball ball) {
        return false;
    }

    @Override
    public void destroy() {

    }
}