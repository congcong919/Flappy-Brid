import bagel.Image;

/**
 * create Empty_Heart class to represent empty heart in game
 */

public class Empty_Heart extends Heart {
    private final Image empty_heart = new Image("res/level/noLife.png");


    /**
     * This is to show life bar after losing life
     * @param Lost_Life This is given outside and represents the number of lost life
     * @param num This is given outside and represents the maximum number of full life with 3 in level0 and
     *  6 in level1
     */
    public void life_update(int Lost_Life, int num){
        int i;
        for (i =0; i <Lost_Life; i++){
            empty_heart.draw(x+(num-1)*heart_gap- i *heart_gap, y);
        }
    }

}
