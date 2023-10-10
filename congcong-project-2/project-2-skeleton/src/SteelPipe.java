import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;

/**
 * Create SteelPipe class extended from PipeSet class to represent steel pipe in game
 */

public class SteelPipe extends PipeSet {
    private final Random random = new Random();
    private final Image PIPE_IMAGE = new Image("res/level-1/steelPipe.png");
    private final Image FLAME = new Image("res/level-1/flame.png");
    private double TOP_PIPE_Y;
    private double BOTTOM_PIPE_Y;
    private double FLAME_TOP_PIPE_Y;
    private double FLAME_BOTTOM_PIPE_Y;
    private final int x = random.nextInt(900);
    private int frame_count;
    private final double Height = PIPE_IMAGE.getHeight();
    private boolean appear = false;


    /**
     * Default steel pipe constructor
     */
    public SteelPipe() {
    }


    /**
     * This is to draw the corresponding pipe with flame
     */
    public void renderPipeSet() {
        frame_count += 1;

        // randomly render steel pipe with different gaps and flame appear per 20 frames and lasts for 30 frames
        if (x%3==0) {
            TOP_PIPE_Y = -PIPE_GAP / 2.0;
            BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0;
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
            FLAME_TOP_PIPE_Y = TOP_PIPE_Y + 0.5 * Height + 10;
            FLAME_BOTTOM_PIPE_Y = BOTTOM_PIPE_Y - 0.5 * Height - 10;
            if (20<=frame_count&&frame_count<=50) {
                appear = true;
                FLAME.draw(pipeX, FLAME_TOP_PIPE_Y);
                FLAME.draw(pipeX, FLAME_BOTTOM_PIPE_Y, ROTATOR);
            }
            if (frame_count>50){
                frame_count = 0;
                appear = false;
            }

        }
        if (x%3==1) {
            TOP_PIPE_Y = -PIPE_GAP / 2.0 - 200;
            BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0 - 200;
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
            FLAME_TOP_PIPE_Y = TOP_PIPE_Y + 0.5 * Height + 10;
            FLAME_BOTTOM_PIPE_Y = BOTTOM_PIPE_Y - 0.5 * Height - 10;
            if (20<=frame_count&&frame_count<=50) {
                appear = true;
                FLAME.draw(pipeX, FLAME_TOP_PIPE_Y);
                FLAME.draw(pipeX, FLAME_BOTTOM_PIPE_Y, ROTATOR);
            }
            if (frame_count>50){
                frame_count = 0;
                appear = false;
            }
        }
        if (x%3==2) {
            TOP_PIPE_Y = -PIPE_GAP / 2.0 + 200;
            BOTTOM_PIPE_Y = Window.getHeight() + PIPE_GAP / 2.0 + 200;
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
            FLAME_TOP_PIPE_Y = TOP_PIPE_Y + 0.5 * Height + 10;
            FLAME_BOTTOM_PIPE_Y = BOTTOM_PIPE_Y - 0.5 * Height - 10;
            if (20<=frame_count&&frame_count<=50) {
                appear = true;
                FLAME.draw(pipeX, FLAME_TOP_PIPE_Y);
                FLAME.draw(pipeX, FLAME_BOTTOM_PIPE_Y, ROTATOR);
            }
            if (frame_count>50){
                frame_count = 0;
                appear = false;
            }
        }
    }



    /**
     * This is to create the rectangle of top steel pipe
     * @return Rectangle This represents the rectangle of top steel pipe
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));

    }



    /**
     * This is to create the rectangle of bottom steel pipe
     * @return Rectangle This represents the rectangle of bottom steel pipe
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));

    }



    /**
     * This is to get the type of the pipe
     * @return String This is the type of the pipe
     */
    public String getPIPE_TYPE(){
        return "steel";
    }



    /**
     * This is to create the rectangle of top flame
     * @return Rectangle This represents the rectangle of top flame
     */
    public Rectangle getFLAME_TopBox() {
        return FLAME.getBoundingBoxAt(new Point(pipeX, FLAME_TOP_PIPE_Y));

    }


    /**
     * This is to create the rectangle of bottom flame
     * @return Rectangle This represents the rectangle of bottom flame
     */
    public Rectangle getFLAME_BottomBox() {
        return FLAME.getBoundingBoxAt(new Point(pipeX, FLAME_BOTTOM_PIPE_Y));

    }


    /**
     * This is to detect whether flame appears
     * @return boolean This represents whether the flame appears at that time
     */
    public boolean isAppear(){
        return appear;
    }
}
