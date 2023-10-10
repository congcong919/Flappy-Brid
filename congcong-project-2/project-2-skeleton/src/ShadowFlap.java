import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;
import java.util.Random;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 * Please filling your name below
 * @author: Congcong Zhao
 */

public class ShadowFlap extends AbstractGame {
    private final Image BACKGROUND_LEVEL0_IMAGE = new Image("res/level-0/background.png");
    private final Image BACKGROUND_LEVEL1_IMAGE = new Image("res/level-1/background.png");
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final int FONT_SIZE = 48;
    private final int SCORE_MSG_OFFSET = 75;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private Bird bird_level0;
    private Bird bird_level1;
    private final Full_Heart full_heart = new Full_Heart();
    private final Empty_Heart empty_heart = new Empty_Heart();
    private final ArrayList<PipeSet> pipeSets = new ArrayList<>();
    private final ArrayList<Weapon> weaponSets = new ArrayList<>();
    private final ArrayList<Boolean> weapon_pick = new ArrayList<>();
    private final ArrayList<Boolean> collision_flame = new ArrayList<>();
    private final ArrayList<Boolean> collision_pipe = new ArrayList<>();
    private final ArrayList<Boolean> destroyed_pipe = new ArrayList<>();
    private final ArrayList<Boolean> weapon_intersect = new ArrayList<>();
    private final Weapon bom = new Bomb();
    private final Weapon roc = new Rock();
    private final Rectangle[] topPipeBox = new Rectangle[40];
    private final Rectangle[] bottomPipeBox = new Rectangle[40];
    private final Rectangle[] weaponBox = new Rectangle[40];
    private Rectangle Box;
    private Rectangle birdBox;
    private Weapon has_weapon;
    private boolean gameOn;
    private boolean win;
    private boolean level_up;
    private boolean out_of_range = false;
    private boolean dead = false;
    private boolean picked = false;
    private boolean weapon_activated = false;
    private boolean collision_state = false;
    private boolean weapon_destroyed = false;
    private int game_frame;
    private int buffer_time;
    private int Lost_Life;
    private int score;
    private int index;
    private int weapon_type;
    private int weapon_frames;
    private int limit = 40;
    private int timescale = 1;
    private int pipe_count = 1;
    private double start_point;
    private double weapon_count;
    private double weapon_spawn = 50;
    private double pipe_spawn = 100;

    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird_level0 = new Bird();
        bird_level1 = new Level1_Bird();
        score = 0;
        gameOn = false;
        win = false;
        level_up = false;
        initial_setup();
    }

    /**
     * The entry point for the program.
     */

    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * two levels, level0 and level1
     * allows bird to pick weapon in order to destroy pipes when S is pressed
     * allows timescale to control the spawn of pipes and movement of weapons and pipes
     */

    @Override
    public void update(Input input) {
        int j;

        // press ESCAPE to exit game
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // press L to speed up the movement of pipe, weapon as well as the spawn of pipe
        if (input.wasPressed(Keys.L)){
            rate_speed_up();
        }

        // press K to speed down the movement of pipe, weapon as well as the spawn of pipe
        if (input.wasPressed(Keys.K)){
            rate_speed_down();
        }

        // press S to use weapon to shoot
        if (input.wasPressed(Keys.S) && level_up && picked){
            bom.set_X();
            roc.set_X();
            start_point = bird_level1.getY();
            weapon_activated = true;
            picked = false;
            weapon_type = has_weapon.getWeapon_type();
        }

        // Level 0 final screen lasts 150 frames before Level 1 initial screen is rendered
        if (level_up) {
            buffer_time += 1;
        }

        if (buffer_time > 150) {
            BACKGROUND_LEVEL1_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        } else {
            BACKGROUND_LEVEL0_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        }


        // game has not started
        if (!gameOn || (win && level_up && buffer_time > 150 && score == 10)) {

            // reset attribute from level0
            level1_state_reset();

            // prepare for pipe, weapon and flame spawn
            if (level_up){
                level_1_setup();
            }

            // show initial screen of level0 or level1
            renderInstructionScreen(input);
        }

        // game over
        if (Lost_Life == 3 && !level_up) {
            dead = true;
        }

        if (Lost_Life == 6 && level_up){
            dead = true;
        }

        if (dead) {
            renderGameOverScreen();
        }

        if (birdOutOfBound()){
            if (level_up){
                bird_level1 = new Level1_Bird();
            } else {
                bird_level0 = new Bird();
            }
            Lost_Life += 1;
        }

        // game won
        if (win) {
            renderWinScreen();
        }

        // game is active
        if (gameOn && !dead && !win && !birdOutOfBound()){
            game_frame += 1;

            // update level0 or level1 bird, if bird picks weapon also draw weapon
            if (level_up){
                bird_level1.update(input);
                if (picked){
                    has_weapon.weapon_update(input, bird_level1);
                }
                birdBox = bird_level1.getBox();
            } else {
                bird_level0.update(input);
                birdBox = bird_level0.getBox();
            }

            // initial pipe spawn rate
            if (game_frame % Math.round(pipe_spawn) == 0){
                pipe_count += 1;
            }

            // initial weapon spawn rate
            if (level_up) {
                if (game_frame % Math.round(weapon_spawn) == 0 && game_frame % Math.round(pipe_spawn) != 0) {
                    weapon_count += 1;
                }
            }

            // draw positions of pipes and weapons
            pipe_render();
            weapon_render();

            // if weapon is activated draw weapon performance
            if (weapon_activated) {
                weapon_performance();
            }

            // remove all corresponding pipes, flames and weapons states when collision happens
            for (j = 0; j < limit; j++) {
                if (collision_pipe.get(j) || collision_flame.get(j)) {
                    flame_pipe_remove(j);
                }

                if (destroyed_pipe.get(j)) {
                    weapon_destroyed(j);
                }
            }

            // show the life bar in level0 or level1 and update the score
            render_life_bar();
            updateScore(birdBox);
            collision_state = false;
            weapon_destroyed = false;
        }

    }



    /**
     * This is to detect whether the bird is out of bound
     * @return boolean This is a state to determine whether the bird is out of bound
     */
    public boolean birdOutOfBound() {
        if (level_up){
            return (bird_level1.getY() > Window.getHeight()) || (bird_level1.getY() < 0);
        } else {
            return (bird_level0.getY() > Window.getHeight()) || (bird_level0.getY() < 0);
        }
    }


    /**
     * This is to show instruction screen when game starts
     * @param input This is given from keyboard when SPACE is pressed at the beginning of the game
     */
    public void renderInstructionScreen(Input input) {
        // paint the instruction on screen
        String INSTRUCTION_MSG = "PRESS SPACE TO START";
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(INSTRUCTION_MSG)/2.0)),
                (Window.getHeight() / 2.0 - (FONT_SIZE / 2.0)));

        // when level up show additional message
        if (level_up){
            String ADDITIONAL_MSG = "PRESS 'S' TO SHOOT";
            FONT.drawString(ADDITIONAL_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(ADDITIONAL_MSG) / 2.0)),
                    (Window.getHeight() / 2.0 - (FONT_SIZE / 2.0)) + SCORE_MSG_OFFSET);
        }
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
            game_frame = 0;
        }
    }


    /**
     * This is to show the game over information and final score screen
     */
    public void renderGameOverScreen() {
        String GAME_OVER_MSG = "GAME OVER!";
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(GAME_OVER_MSG) / 2.0)),
                (Window.getHeight() / 2.0 - (FONT_SIZE / 2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth() / 2.0 - (FONT.getWidth(finalScoreMsg) / 2.0)),
                (Window.getHeight() / 2.0 - (FONT_SIZE / 2.0)) + SCORE_MSG_OFFSET);
    }


    /**
     * This is to show the game win information and final score screen
     */
    public void renderWinScreen() {
        if (level_up && score == 10){
            String LEVEL_UP_MSG = "LEVEL UP!";
            FONT.drawString(LEVEL_UP_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(LEVEL_UP_MSG) / 2.0)),
                    (Window.getHeight() / 2.0 - (FONT_SIZE / 2.0)));
        } else {
            String CONGRATS_MSG = "CONGRATULATIONS!";
            FONT.drawString(CONGRATS_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(CONGRATS_MSG) / 2.0)),
                    (Window.getHeight() / 2.0 - (FONT_SIZE / 2.0)));
        }
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth() / 2.0 - (FONT.getWidth(finalScoreMsg) / 2.0)),
                (Window.getHeight() / 2.0 - (FONT_SIZE / 2.0)) + SCORE_MSG_OFFSET);
        dead = false;
        reset_timescale();
    }


    /**
     * This is to detect the collision between pipe and bird
     * @param birdBox given based on current bird position
     * @param topPipeBox given based on current top pipe position
     * @param bottomPipeBox given based on current bottom pipe position
     * @return boolean This is the state whether collision happens
     */
    public boolean detectCollision(Rectangle birdBox, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        if (birdBox != null) {
            return birdBox.intersects(topPipeBox) || birdBox.intersects(bottomPipeBox);
        } else {
            return false;
        }
    }



    /**
     * This is to update score when bird safely passes through the whole pipe or weapon destroys pipe
     * @param birdBox This is given based on current bird position
     */
    public void updateScore(Rectangle birdBox) {

        // level0 detect whether bird pass through the whole pipe and then pipe index plus 1
        if (!level_up) {
            if (!collision_state && birdBox.left() > topPipeBox[index].right()) {
                score += 1;
                index += 1;
            }

            if (score == 10 && !level_up) {
                win = true;
                level_up = true;
                pipeSets.clear();
            }
        }

        // level1 detect whether bird pass through whole pipe or use weapon to destroy pipe
        if (level_up) {
            if (!collision_state && birdBox.left() > topPipeBox[index].right()) {
                score += 1;
                index += 1;
            }

            if (weapon_destroyed){
                score += 1;
            }
            // detect win
            if (score == 30 && level_up) {
                win = true;
            }
        }

        // updated score message
        String SCORE_MSG = "SCORE: ";
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, 100, 100);
    }


    /**
     * This is to draw the whole pipe sets in level0 as well as corresponding collision state or destroyed state
     */
    public void initial_setup(){
        int i;
        for (i = 0; i < limit; i++) {
            pipeSets.add(new PlasticPipe());
            collision_pipe.add(false);
            collision_flame.add(false);
            destroyed_pipe.add(false);
        }
    }



    /**
     * This is to draw randomly different pipe sets in level1 as well as corresponding collision state,
     * flame collision state or destroyed state
     * Also the spawn of the weapon in level1 and their weapon picked state
     */
    public void level_1_setup(){
        int x;
        int j;
        Random random = new Random();
        for (j = 0; j < limit; j++) {
            x = random.nextInt(999);
            if (x % 2 == 0) {
                pipeSets.add(new PlasticPipe());
                weaponSets.add(new Rock());
            } else {
                pipeSets.add(new SteelPipe());
                weaponSets.add(new Bomb());
                collision_pipe.add(false);
                collision_flame.add(false);
                weapon_pick.add(false);
                destroyed_pipe.add(false);
                weapon_intersect.add(false);
            }
        }
    }


    /**
     * This is to reset some attributes in level0
     */
    public void level1_state_reset(){
        win = false;
        gameOn = false;
        pipe_count = 1;
        score = 0;
        Lost_Life = 0;
    }


    /**
     * This is to detect whether there is a collision between bird and flame
     * @param birdBox This is given based on current position of bird
     * @param pipeSets This is one type of pipe bird encounters
     * @return boolean This is the state whether the bird collides with the flame
     */
    public boolean detect_flame_Collision(Rectangle birdBox, PipeSet pipeSets){

        // if a given pipe is steel pipe then return whether the bird collides with flame
        // else return false

        if (pipeSets.isAppear() && pipeSets.getPIPE_TYPE().equals("steel")) {
            return birdBox.intersects(pipeSets.getFLAME_TopBox()) || birdBox.intersects(pipeSets.getFLAME_BottomBox());
        } else {
            return false;
        }
    }



    /**
     * This is to detect whether the bird intersects with a weapon
     * @param birdBox This is given based on current position of bird
     * @param weaponBox This is the position of a weapon
     * @return boolean This is to determine whether the bird intersects a weapon
     */
    public boolean detect_weapon_pick(Rectangle birdBox, Rectangle weaponBox) {
        return birdBox.intersects(weaponBox);
    }



    /**
     * This is to detect pipe type and based on current weapon picked to determine whether the pipe can be destroyed
     * by a weapon
     * @param pipe This is one type of pipe
     * @return boolean This is a state to represent whether weapon can destroy specific type of pipe
     */
    public boolean detect_type(PipeSet pipe) {
        if (weapon_type == 1) {
            return true;
        }

        if (weapon_type == 0) {
            return pipe.getPIPE_TYPE().equals("plastic");
        }
        return false;
    }



    /**
     * This is draw the life bar
     */
    public void render_life_bar(){
        if (level_up){
            int level1_heart_num = 6;
            full_heart.heart_render(level1_heart_num);
            empty_heart.life_update(Lost_Life, level1_heart_num);
        } else {
            int level0_heart_num = 3;
            full_heart.heart_render(level0_heart_num);
            empty_heart.life_update(Lost_Life, level0_heart_num);
        }
    }



    /**
     * This is to reset timescale which affects the spawn rate and movement of pipe and weapon after level0 finished
     */
    public void reset_timescale(){
        index = 0;
        pipe_spawn = 100;
        weapon_spawn = pipe_spawn / 2;
        timescale = 1;
        PipeSet.setPipeSpeed(5);
        Weapon.setWeaponSpeed(5);
    }



    /**
     * This is to speed up the spawn rate of pipe and weapon as well as the movement of pipe and weapon
     */
    public void rate_speed_up(){
        timescale += 1;
        if (timescale > 5) {
            timescale = 5;
        } else {
            PipeSet.PIPE_SPEED_rate(true);
            Weapon.WEAPON_SPEED_rate(true);
            pipe_spawn = pipe_spawn * 0.5;
        }
        weapon_spawn = pipe_spawn / 2;
    }



    /**
     * This is to speed down the spawn rate of pipe and weapon as well as the movement of pipe and weapon
     */
    public void rate_speed_down(){
        timescale -= 1;
        if (timescale < 1) {
            timescale = 1;
            pipe_spawn = 100;
        } else {
            PipeSet.PIPE_SPEED_rate(false);
            Weapon.WEAPON_SPEED_rate(false);
            pipe_spawn = pipe_spawn / 0.5;
        }
        weapon_spawn = pipe_spawn / 2;
    }



    /**
     * This is to draw the weapon and store the attribute of the weapon when is picked by a bird
     */
    public void weapon_render(){
        int j;
        for (j = 0; j < weapon_count; j++) {

            // draw the weapon set
            weaponSets.get(j).update();
            weaponBox[j] = weaponSets.get(j).getWeaponBox();
            weapon_pick.set(j, detect_weapon_pick(birdBox, weaponBox[j]));

            // if weapon is picked then store weapon original weapon position disappears
            if (weapon_pick.get(j) && !picked) {
                has_weapon = weaponSets.get(j);
                weapon_frames = 0;
                weaponSets.remove(j);
                weapon_pick.remove(j);
                weapon_count -= 1;
                picked = true;
            }
        }
    }



    /**
     * This is to draw the pipe set check the state of pipe collision, flame collision and pipe destroyed by a weapon
     */
    public void pipe_render(){
        int j;
        for(j = 0; j < pipe_count; j++) {

            // draw the pipe set
            pipeSets.get(j).update();
            topPipeBox[j] = pipeSets.get(j).getTopBox();
            bottomPipeBox[j] = pipeSets.get(j).getBottomBox();
            collision_pipe.set(j, detectCollision(birdBox, topPipeBox[j], bottomPipeBox[j]));

            // check flame collision state for each corresponding pipe
            if (level_up){
                collision_flame.set(j, detect_flame_Collision(birdBox, pipeSets.get(j)));
            }

            // check if the picked weapon destroys corresponding pipe
            if (weapon_activated){
                if (detect_type(pipeSets.get(j))) {
                    destroyed_pipe.set(j,detectCollision(Box, topPipeBox[j], bottomPipeBox[j]));
                }
                weapon_intersect.set(j, detectCollision(Box, topPipeBox[j], bottomPipeBox[j]));

                // if a pipe destroyed by an appropriate weapon or not or out of range then
                // reset weapon state

                if (destroyed_pipe.get(j) || (!destroyed_pipe.get(j) && weapon_intersect.get(j)) || out_of_range){
                    weapon_activated = false;
                    Box = null;
                    out_of_range = false;
                }
            }
        }
    }



    /**
     * This is to draw corresponding shoot range of weapon when weapon is flying and
     * create position based rectangle used to detect collision
     */
    public void weapon_performance(){
        weapon_frames += 1;
        if (weapon_type == 1 ) {
            if (weapon_frames <= 50) {
                Box = bom.attack(start_point);
            } else {
                out_of_range = true;
                Box = null;
            }
        }

        if (weapon_type == 0 ) {
            if (weapon_frames <= 25) {
                Box = roc.attack(start_point);
            } else {
                out_of_range = true;
                Box = null;
            }
        }
    }



    /**
     * This is to remove the whole pipe when flame collision or pipe collision happens
     * @param j This is the index of corresponding pipe
     */
    public void flame_pipe_remove(int j){
        limit -= 1;
        pipeSets.remove(j);
        collision_pipe.remove(j);
        pipe_count = pipe_count - 1;
        collision_state = true;

        // when collides with flame or pipe record lost life
        Lost_Life = Lost_Life + 1;
        if (level_up) {
            collision_flame.remove(j);
        }
    }



    /**
     * This is to remove corresponding pipe state when pipe is destroyed by the weapon
     * @param j This is the corresponding index of a weapon picked
     */
    public void weapon_destroyed(int j){
        pipeSets.remove(j);
        destroyed_pipe.remove(j);
        collision_pipe.remove(j);
        weapon_destroyed = true;
        pipe_count = pipe_count - 1;
    }
}
