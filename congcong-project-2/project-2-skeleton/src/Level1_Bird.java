import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;


/**
 * Modified from project 1 solution Bird class
 * represent a level1 bird in game with several methods to get bird information
 * Extended from Bird class
 */

public class Level1_Bird extends Bird{
    private final Image WING_DOWN_IMAGE = new Image("res/level-1/birdWingDown.png");
    private final Image WING_UP_IMAGE = new Image("res/level-1/birdWingUp.png");
    private int frameCount = 0;
    private double y;
    private double yVelocity;
    private Rectangle boundingBox;

    /**
     * Default constructor of a level1 bird
     */
    public Level1_Bird() {
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
    }

    /**
     * draw the position of level1 bird everytime called and create rectangle based on level1 bird position
     * @param  input This is from the keyboard
     * @return Rectangle This is a position of level1 bird as a rectangle
     */
    public Rectangle update(Input input) {
        frameCount += 1;

        // when SPACE is pressed bird fly up
        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            WING_DOWN_IMAGE.draw(X, y);
        }
        else {

            // falling speed cannot exceed 10
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);

            // flap wings per 10 frame
            if (frameCount % SWITCH_FRAME == 0) {
                WING_UP_IMAGE.draw(X, y);
                boundingBox = WING_UP_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
            else {
                WING_DOWN_IMAGE.draw(X, y);
                boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
        }
        y += yVelocity;

        return boundingBox;
    }

    /**
     * Return Y-coordinate of the level1 bird
     * @return double This is to get the Y-coordinate of the level1 bird
     */
    public double getY() {
        return y;
    }


    /**
     * Return X-coordinate of the level1 bird
     * @return double This is to get the X-coordinate of the level1 bird
     */
    public double getX() {
        return X;
    }


    /**
     * Return level1 bird position as a rectangle
     * @return Rectangle This is to get the position of the level1 bird as the centre of rectangle
     */
    public Rectangle getBox() {
        return boundingBox;
    }
}
