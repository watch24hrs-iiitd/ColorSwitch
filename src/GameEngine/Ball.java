package GameEngine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {

    private final double jumpSpeed;
    private final double gravity;
    private final double radius;
    private final double x;
    private final double midLine;

    private double y;
    private double velocity;
    private Color color;
    private final GraphicsContext graphicsContext;

    Ball(GraphicsContext graphicsContext) {
        x = 225;
        y = 600;
        radius = 10;
        velocity = 0;
        jumpSpeed = -500;
        gravity = 1900;
        midLine = 350;
        color = Color.RED;
        this.graphicsContext = graphicsContext;
    }

    public void refresh() {
        graphicsContext.setFill(color);
        graphicsContext.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    public void jump() {
        velocity = jumpSpeed;
    }

    public double move(double time) {
        // TODO : tweaks to make jump animation good
        double distance = velocity * time + (gravity * time * time)/2;
        velocity += gravity * time;
        y += distance;

        // TODO : add hand
        if (y > 600) {
            y = 600;
            velocity = 0;
        }
        if (y < midLine) {
            double te = y;
            y = midLine;
            return midLine - te;
        }
        return 0;
    }

}
