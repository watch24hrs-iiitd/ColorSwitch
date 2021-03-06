package gameEngine.gameElements.obstacles;

import gameEngine.Ball;
import gameEngine.util.Renderer;
import gameEngine.gameElements.Star;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class ObsCircle extends Obstacle {

    protected final double radius;
    protected final double innerRadius;
    protected double rotationAngle;

    protected ArrayList<String> colors = new ArrayList<>()
    {{
        add("F6DF0E");
        add("8E11FE");
        add("32E1F4");
        add("FD0082");
    }};

    public ObsCircle(double x, double y, double radius, double width) {
        super(x, y, y - radius, y + radius);
        star = new Star(x, y);
        this.radius = radius;
        this.innerRadius = radius - width;
        rotationAngle = 0;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void refresh(GraphicsContext graphicsContext) {
        if (star != null) {
            star.refresh(graphicsContext);
        }
        graphicsContext.translate(getX(), getY());
        graphicsContext.rotate(-rotationAngle);
        int angle = 0;
        for (String color : colors) {
            Renderer.drawArc(0, 0, radius, innerRadius, Color.web(color), Color.web(color), angle++);
        }
        graphicsContext.rotate(rotationAngle);
        graphicsContext.translate(-getX(), -getY());
    }


    @Override
    public void update(double time) {
        rotationAngle += rotationalSpeed * time;
        while(rotationAngle < 0) {
            rotationAngle += 360;
        }
        rotationAngle %= 360;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    @Override
    public boolean checkCollision(Ball ball) {

        double top = ball.getY() - ball.getRadius();
        double bottom = ball.getY() + ball.getRadius();
        int angle = (int)rotationAngle % 360;
        boolean isCollided = false;

        if ((top < getY() + radius && bottom > getY() + innerRadius)) {

            if (angle > 355 || (angle > 0 && angle < 95)) {
                isCollided = checkNotEqual(colors.get(2), ball.getColor());
            }
            if (angle > 85 && angle < 185) {
                isCollided |= checkNotEqual(colors.get(1), ball.getColor());
            }
            if (angle > 175 && angle < 275) {
                isCollided |= checkNotEqual(colors.get(0), ball.getColor());
            }
            if ((angle > 265) || (angle < 5)) {
                isCollided |= checkNotEqual(colors.get(3), ball.getColor());
            }
        } else if ((top < getY() - innerRadius && bottom > getY() - radius)) {

            if (angle > 355 || (angle > 0 && angle < 95)) {
                isCollided = checkNotEqual(colors.get(0), ball.getColor());
            }
            if (angle > 85 && angle < 185) {
                isCollided |= checkNotEqual(colors.get(3), ball.getColor());
            }
            if (angle > 175 && angle < 275) {
                isCollided |= checkNotEqual(colors.get(2), ball.getColor());
            }
            if ((angle > 265) || (angle < 5)) {
                isCollided |= checkNotEqual(colors.get(1), ball.getColor());
            }
        }

        return isCollided;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public void rotate(double angle) {
        rotationAngle += angle;
        while(rotationAngle < 0) {
            rotationAngle += 360;
        }
        rotationAngle %= 360;
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getRandomColor() {
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }

    public void mirrorY() {
        // mirror Image of circle
        colors = new ArrayList<>()
        {{
            add("F6DF0E");
            add("FD0082");
            add("32E1F4");
            add("8E11FE");
        }};
    }

    public ObsCircle generateNext() {
        return new ObsCircle(x, y - 2 * radius - 5, radius, radius - innerRadius);
    }

}
