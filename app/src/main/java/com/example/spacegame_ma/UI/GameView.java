package com.example.spacegame_ma.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.spacegame_ma.Entity.Bullet;
import com.example.spacegame_ma.Entity.Enemy;
import com.example.spacegame_ma.Entity.Player;
import com.example.spacegame_ma.Logic.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean isRunning;
    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private float enemySpeed = 2;
    private int cooldown = 10;
    private int maxEnemyTimer = 60;
    private int enemyTimer;
    private Rect bulletShape;

    private Random random;


    public GameView(Context context) {
        super(context);
        init(context);
        player = new Player(this);

        player.update(player.move());
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        random = new Random();

        enemyTimer = maxEnemyTimer;



    }

    public void init(Context context){
        Constants.CURRENT_CONTEXT = context;
        System.out.println(("init"));
    }

    @Override
    public void run() {

        while(isRunning){

            spawnEnemy();
            update();
            draw();
            sleep();

        }
    }

    public void update(){
        player.update(player.move());
        List<Bullet> offscreenBullets = new ArrayList<>();
        List<Enemy> offscreenEnemies = new ArrayList<>();

        if(isGameOver()){
            isRunning = false;
        }

        for (Bullet bullet : bullets){

            bulletShape = new Rect(bullet.move().x-25, bullet.move().y -25, bullet.move().x+25,bullet.move().y+25);
            bullet.update(bullet.move());

            for(Enemy enemy : enemies){
                if(Rect.intersects(bulletShape, enemy.collisionShape())){
                    enemy.move().y = Constants.SCREEN_HEIGHT +100;
                    offscreenBullets.add(bullet);
                }
            }
            if(bullet.move().y < 0){
                offscreenBullets.add(bullet);
            }
        }

        for (Enemy enemy : enemies){
            if(enemy.move().y > Constants.SCREEN_HEIGHT +10){
                offscreenEnemies.add(enemy);
            }
            enemy.update((enemy.move()));

        }
        for(Enemy enemy : offscreenEnemies){
            enemies.remove((enemy));
        }
        for(Bullet bullet : offscreenBullets){
            bullets.remove(bullet);
        }

        cooldown --;
        enemyTimer--;

    }

    public void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.WHITE);

            for (Bullet bullet : bullets){
                bullet.draw(canvas);
            }
            for(Enemy enemy : enemies){
                enemy.draw(canvas);
            }
            player.draw(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }

    }
    private void sleep(){
        //cap at ~60 FPS
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resume(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){
        try {
            isRunning = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(cooldown<=0){
            newBullet();
            spawnEnemy();
            cooldown = 10;
        }

        return true;
    }


    public void newBullet(){
        Bullet bullet = new Bullet(player.move().x, player.move().y, Color.rgb(255, 175, 0));
        bullets.add(bullet);
    }

    public void spawnEnemy(){
        if (enemyTimer <= 0){
            Enemy enemy = new Enemy(4);

            enemies.add(enemy);
            if(maxEnemyTimer>18){
                maxEnemyTimer--;
            }
            enemySpeed += 0.1f;
            System.out.println(maxEnemyTimer);
            enemyTimer = maxEnemyTimer;
        }


    }

    public boolean isGameOver(){
        //gameover if player hit by enemy
        boolean gameOver = false;
        for (Enemy enemy : enemies){

            if(Rect.intersects(player.collisionShape(),enemy.collisionShape())){
                gameOver = true;
            }
        }
        return gameOver;
    }
}
