import bagel.Window;
import bagel.util.Rectangle;
import bagel.Input;

/**
 * Create an abstract Weapon class to represent weapon in the game
 */

public abstract class Weapon {
    protected static double weapon_speed = 5;
    protected double weaponX = Window.getWidth();
    protected final double weapon_attack_speed = 5;
    protected double X = 227.5;


    /**
     * Default Weapon constructor
     */
    public Weapon(){
    }

    /**
     * This is used to draw updated movement of the weaponSets
     */
    public void update() {
        renderWeapon();
        weaponX -= weapon_speed;
    }

    /**
     * abstract method to draw the corresponding Weapon
     */
    public abstract void renderWeapon();



    /**
     *This is used to change the movement of weapon after timescale is used
     * @param state This is got from the outside
     */
    public static void WEAPON_SPEED_rate(Boolean state){
        if (state) {
            weapon_speed = weapon_speed * 1.5;
        }
        if (!state) {
            weapon_speed = weapon_speed / 1.5;
        }
    }

    /**
     * Create rectangle based on weapon position
     * @return Rectangle This is based on weapon position
     */
    public abstract Rectangle getWeaponBox();



    /**
     * This is used to reset weapon speed to its original speed
     * @param num This is got from given speed
     * @return double reset to original speed
     */
    public static double setWeaponSpeed(double num){
        return weapon_speed = num;
    }



    /**
     * This is used to draw the initial position of weapon after weapon is picked by bird
     * @param input This is given from keyboard
     * @param birdBox This is given based on current bird position
     */
    public abstract void weapon_update(Input input, Bird birdBox);



    /**
     * This is to draw the performance of the weapon after being activated
     * @param Y This is given based on starting attacking position
     * @return Rectangle This represents the rectangle based on weapon position when weapon is flying
     */
    public abstract Rectangle attack(double Y);



    /**
     * This is to get weapon type 1 == bomb,  0 == rock
     * @return int get type of bomb
     */
    public abstract int getWeapon_type();


    /**
     * This is to reset the original X-position of weapon when weapon is picked
     */
    public abstract void set_X();


}
