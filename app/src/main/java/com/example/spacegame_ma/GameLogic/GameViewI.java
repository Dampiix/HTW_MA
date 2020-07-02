package com.example.spacegame_ma.GameLogic;

import com.example.spacegame_ma.Entity.Bullet;
import com.example.spacegame_ma.Entity.Enemy;
import com.example.spacegame_ma.Entity.Player;

public interface GameViewI {


    /**
    updates positions of entities to draw the next frame
     */
    void update();

    /**
    draw everything on the canvas
     */
    void draw();

    /**
    *checks if a bullet collides with a enemy
    *
     * @return boolean
    */
    boolean enemyHit(Bullet b, Enemy e);

    /**
    checks if the player collides with a enemy --> game over

     @return boolean, hit = true | no hit = false
     */
    boolean isGameOver(Player p, Enemy e);

    /**
    @return score
     */
    int getScore();

    /**
     * waits to get ~60 fps
     */
    void sleep();




    /**
     * creates a new Bullet at the player Position
     */
    void newBullet();

    /**
     * spawns a new enemy
     */
    void spawnEnemy();

    /**
     * spawns the player at the start of the game
     */
    void spawnPlayer();
}
