import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;

/**
 * Create a Rock class extended from Weapon class to represent rock in the game
 */

public class Rock extends Weapon{
    private final Random random = new Random();
    private final int x = random.nextInt(900);
    private final double weapon_Y = 100 + (Math.random() * 400);
    private final Image ROCK_IMAGE = new Image("res/level-1/rock.png");
    private double Y;


    /**
     * Default rock constructor
     */
    public Rock(){
    }


    /**
     * This is to draw the corresponding Weapon
     */
    public void renderWeapon(){
        if (x %2==0) {
            Y = weapon_Y;
            ROCK_IMAGE.draw(weaponX, weapon_Y);
        }
    }


    /**
     * Create rectangle based on weapon position
     * @return Rectangle This is based on weapon position
     */
    public Rectangle getWeaponBox() {
        return ROCK_IMAGE.getBoundingBoxAt(new Point(weaponX, Y));
    }


    /**
     * This is used to draw the initial position of weapon after weapon is picked by bird
     * @param input This is given from keyboard
     * @param birdBox This is given based on current bird position
     */
    public void weapon_update(Input input, Bird birdBox){
        ROCK_IMAGE.draw(birdBox.getX()+0.5*birdBox.get_Width(), birdBox.getY());
    }


    /**
     * This is to get weapon type  0 == rock
     * @return int get type of rock
     */
    public int getWeapon_type(){
        return 0;
    }


    /**
     * @param Y_point This is given based on starting attacking position
     * @return Rectangle This represents the rectangle based on rock position when rock is flying
     */
    public Rectangle attack(double Y_point){
        Rectangle Box;
        ROCK_IMAGE.draw(X, Y_point);
        Box = ROCK_IMAGE.getBoundingBoxAt(new Point(X, Y_point));
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
