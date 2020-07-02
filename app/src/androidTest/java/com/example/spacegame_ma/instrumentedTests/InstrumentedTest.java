package com.example.spacegame_ma.instrumentedTests;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.spacegame_ma.Constants.Constants;
import com.example.spacegame_ma.Entity.Bullet;
import com.example.spacegame_ma.Entity.Enemy;
import com.example.spacegame_ma.Entity.Player;
import com.example.spacegame_ma.GameLogic.GameView;


import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    private Context context = ApplicationProvider.getApplicationContext();
    private int width = 1080;
    private int height = 1920;

    @Test
    public void GameOverTest(){
        Constants.SCREEN_HEIGHT = height;
        Constants.SCREEN_WIDTH = width;
        GameView gameView = new GameView(context);

        Player player = new Player(gameView);
        Enemy enemy = new Enemy(40);

        enemy.move().y = player.move().y;
        enemy.move().x = player.move().x;

        assertEquals(true , gameView.isGameOver(player,enemy));
    }

    @Test
    public void notGameOverTest(){
        Constants.SCREEN_HEIGHT = height;
        Constants.SCREEN_WIDTH = width;
        GameView gameView = new GameView(context);

        Player player = new Player(gameView);
        Enemy enemy = new Enemy(40);

        enemy.move().y = player.move().y;
        enemy.move().x = player.move().x + 100;

        assertEquals(false , gameView.isGameOver(player,enemy));
    }

    @Test
    public void enemyHitByBullet(){
        Constants.SCREEN_HEIGHT = height;
        Constants.SCREEN_WIDTH = width;
        GameView gameView = new GameView(context);

        Player player = new Player(gameView);
        Enemy enemy = new Enemy(40);

        enemy.move().y = player.move().y -200;
        enemy.move().x = player.move().x;

        Bullet bullet = new Bullet(enemy.move().x, enemy.move().y);

        assertEquals(true,gameView.enemyHit(bullet, enemy));
    }

    @Test
    public void bulletMissesEnemy(){
        Constants.SCREEN_HEIGHT = height;
        Constants.SCREEN_WIDTH = width;
        GameView gameView = new GameView(context);

        Player player = new Player(gameView);
        Enemy enemy = new Enemy(40);

        enemy.move().y = player.move().y -200;
        enemy.move().x = player.move().x;

        Bullet bullet = new Bullet(enemy.move().x +100, enemy.move().y);

        assertEquals(false,gameView.enemyHit(bullet, enemy));
    }

    @Test
    public void currentScoreIncreaseTest(){
        Constants.SCREEN_HEIGHT = height;
        Constants.SCREEN_WIDTH = width;
        GameView gameView = new GameView(context);

        Player player = new Player(gameView);
        Enemy enemy = new Enemy(40);

        enemy.move().y = player.move().y -200;
        enemy.move().x = player.move().x;

        Bullet bullet = new Bullet(enemy.move().x, enemy.move().y);
        assertEquals(true, gameView.enemyHit(bullet, enemy));
        assertEquals(1, gameView.getScore());
    }



}
