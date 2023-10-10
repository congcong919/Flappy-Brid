import bagel.Image;


/**
 * create Full_Heart class to represent full heart in game
 */

public class Full_Heart extends Heart{
    private final Image full_heart = new Image("res/level/fullLife.png");


    /**
     * This is to show the remaining life
     * @param num This is given from the outside 3 for level0 and 6 for level1
     */
    public void heart_render(int num) {
        int i;
        for (i =0; i <num; i++){
            full_heart.draw(x+ i *heart_gap, y);
        }
    }

}
