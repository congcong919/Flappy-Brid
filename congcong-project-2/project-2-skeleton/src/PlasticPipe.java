import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;

/**
 * Create PlasticPipe class extended from PipeSet class to represent plastic pipe in game
 */

public class PlasticPipe extends PipeSet {
    private final Random random = new Random();
    double TOP_PIPE_Y;
    double BOTTOM_PIPE_Y;
    private final int x = random.nextInt(999 );
    private final Image PIPE_IMAGE = new Image("res/level/plasticPipe.png");
    private Rectangle Nothing = null;



    /**
     * Default plastic pipe constructor
     */
    public PlasticPipe() {
    }


    /**
     * This is to draw the corresponding pipe
     */
    public void renderPipeSet() {

        // randomly render plastic pipe with different gaps
        if (x%3==0) {
            TOP_PIPE_Y = -PIPE_GAP / 2.0;
            BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0;
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
        if (x%3==1) {
            TOP_PIPE_Y = -PIPE_GAP / 2.0 - 200;
            BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0 - 200;
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
        if (x%3==2) {
            TOP_PIPE_Y = -PIPE_GAP / 2.0 + 200;
            BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0 + 200;
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
    }



    /**
     * This is to create the rectangle of top plastic pipe
     * @return Rectangle This represents the rectangle of top plastic pipe
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));

    }



    /**
     * This is to create the rectangle of bottom plastic pipe
     * @return Rectangle This represents the rectangle of bottom plastic pipe
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));

    }



    /**
     * This is to get the type of the pipe
     * @return String This is the type of the pipe
     */
    public String getPIPE_TYPE(){
        return "plastic";
    }



    /**
     * This is to detect whether flame appears, in this type of pipe, flame does not appear
     * @return boolean This represents whether the flame appears at that time
     */
    public boolean isAppear(){
        return false;
    }



    /**
     * This is to create the rectangle of top flame, in this type of pipe, does not have flame top box
     * @return Rectangle This represents the rectangle of top flame
     */
    public Rectangle getFLAME_TopBox() {
        return Nothing;
    }



    /**
     * This is to create the rectangle of bottom flame, in this type of pipe, does not have flame bottom box
     * @return Rectangle This represents the rectangle of bottom flame
     */
    public Rectangle getFLAME_BottomBox() {
        return Nothing;
    }

}
