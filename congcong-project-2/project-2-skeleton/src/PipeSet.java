import bagel.DrawOptions;
import bagel.Window;
import bagel.util.Rectangle;

/**
 * Create pipe set class to represent pipes in game
 */

public abstract class PipeSet {
    protected final int PIPE_GAP = 168;
    private static double PIPE_SPEED = 5;
    protected final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    protected double pipeX = Window.getWidth();


    /**
     * Default pipe set constructor
     */
    public PipeSet() {
    }



    /**
     * abstract method to draw the corresponding pipe
     */
    public abstract void renderPipeSet();



    /**
     * This is used to draw updated movement of the pipeSets
     */
    public void update() {
        renderPipeSet();
        pipeX -= PIPE_SPEED;

    }


    /**
     * abstract method to create the rectangle of top pipe
     * @return Rectangle This represents the rectangle of top pipe
     */
    public abstract Rectangle getTopBox();



    /**
     * abstract method to create the rectangle of bottom pipe
     * @return Rectangle This represents the rectangle of bottom pipe
     */
    public abstract Rectangle getBottomBox();



    /**
     * abstract method to get the type of the pipe
     * @return String This is the type of the pipe
     */
    public abstract String getPIPE_TYPE();


    /**
     * abstract method to detect whether flame appears
     * @return boolean This represents whether the flame appears at that time
     */
    public abstract boolean isAppear();



    /**
     * abstract method to create the rectangle of top flame
     * @return Rectangle This represents the rectangle of top flame
     */
    public abstract Rectangle getFLAME_TopBox();



    /**
     * abstract method to create the rectangle of bottom flame
     * @return Rectangle This represents the rectangle of bottom flame
     */
    public abstract Rectangle getFLAME_BottomBox();


    /**
     *This is used to change the movement of pipe after timescale is used
     * @param state This is got from the outside
     */
    public static void PIPE_SPEED_rate(Boolean state){
        if (state) {
            PIPE_SPEED = PIPE_SPEED * 1.5;
        }
        if (!state) {
            PIPE_SPEED = PIPE_SPEED / 1.5;
        }
    }


    /**
     * This is used to reset pipe speed to its original speed
     * @param num This is got from given speed
     * @return double reset to original speed
     */
    public static double setPipeSpeed(double num){
        return PIPE_SPEED = num ;
    }




}
