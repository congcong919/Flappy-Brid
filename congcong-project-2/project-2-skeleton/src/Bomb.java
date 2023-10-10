import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;

/**
 * Create a Bomb class extended from Weapon class to represent bomb in the game
 */

public class Bomb extends Weapon{
    private final Random random = new Random();
    private final int x = random.nextInt(900);
    private final double weapon_Y = 100 + (Math.random() * 400);
    private final Image BOMB_IMAGE = new Image("res/level-1/bomb.png");
    private double Y;


    /**
     * Default bomb constructor
     */
    public Bomb(){
    }


    /**
     * This is to draw the corresponding Weapon
     */
    public void renderWeapon() {

        // randomly render bomb
        if (x % 2 == 0) {
            Y = weapon_Y;
            BOMB_IMAGE.draw(weaponX, weapon_Y);
        }
    }


    /**
     * Create rectangle based on weapon position
     * @return Rectangle This is based on weapon position
     */
    public Rectangle getWeaponBox() {
        return BOMB_IMAGE.getBoundingBoxAt(new Point(weaponX, Y));
    }


    /**
     * This is used to draw the initial position of weapon after weapon is picked by bird
     * @param input This is given from keyboard
     * @param birdBox This is given based on current bird position
     */
    public void weapon_update(Input input, Bird birdBox){
        BOMB_IMAGE.draw(birdBox.getX()+0.5*birdBox.get_Width(), birdBox.getY());
    }


    /**
     * This is to get weapon type 1 == bomb
     * @return int get type of bomb
     */
    public int getWeapon_type(){
        return 1;
    }


    /**
     * @param Y_point This is given based on starting attacking position
     * @return Rectangle This represents the rectangle based on bomb position when bomb is flying
     */
    public Rectangle attack(double Y_point){
        Rectangle Box;
        BOMB_IMAGE.draw(X, Y_point);
        Box = BOMB_IMAGE.getBoundingBoxAt(new Point(X, Y_point));
        X += weapon_attack_speed;
        return Box;
    }


    /**
     * This is to reset the original X-position of weapon when weapon is picked
     */
    public void set_X(){
        X = 227.5;
    }
}
