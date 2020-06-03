package com.example.spacegame_ma.GameLogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.spacegame_ma.Entity.Bullet;
import com.example.spacegame_ma.Entity.Enemy;
import com.example.spacegame_ma.Entity.Player;
import com.example.spacegame_ma.Constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private int score = 0;
    private int highscore;
    private boolean gameOver = false;

    private Random random;


    public GameView(Context context) {
        super(context);
        init(context);
        spawnPlayer();

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
        spawnEnemy();
        player.update(player.move());
        List<Bullet> offscreenBullets = new ArrayList<>();
        List<Enemy> offscreenEnemies = new ArrayList<>();

        if(gameOver){
            isRunning = false;
        }

        for (Bullet bullet : bullets){

            bullet.update(bullet.move());
            bulletShape = new Rect(bullet.move().x-25, bullet.move().y -25, bullet.move().x+25,bullet.move().y+25);
            for(Enemy enemy : enemies){
                if(enemyHit(bullet, enemy)){
                    enemy.move().y = Constants.SCREEN_HEIGHT +100;
                    score++;
                    compareScore(score);
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
            isGameOver(player, enemy);
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
            canvas.drawColor(Color.rgb(10,10,44));

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
            cooldown = 10;
        }
        return true;
    }


    private void newBullet(){
        Bullet bullet = new Bullet(player.move().x, player.move().y);
        bullets.add(bullet);
        System.out.println(bullet.speed);

    }

    private void spawnEnemy(){
        if (enemyTimer <= 0){
            Enemy enemy = new Enemy(4);

            enemies.add(enemy);
            if(maxEnemyTimer>18){
                maxEnemyTimer--;
            }
            enemySpeed += 0.1f;
            enemyTimer = maxEnemyTimer;
        }
    }

    private void spawnPlayer(){
        player = new Player(this);
    }

    public boolean enemyHit(Bullet b, Enemy e){
        bulletShape = new Rect(b.move().x-25, b.move().y -25, b.move().x+25,b.move().y+25);
        if(Rect.intersects(bulletShape, e.collisionShape())){
            return true;
        }else{
            return false;
        }
    }


    public boolean isGameOver(Player p, Enemy e){
        //gameover if player hit by enemy
        if(Rect.intersects(p.collisionShape(),e.collisionShape())){
            gameOver = true;
            System.out.println("HIGHSCORE: " + highscore);
        }
        return gameOver;
    }

    public int compareScore(int score){
        if(score >= highscore){
            highscore = score;
        }
        return highscore;
    }
}
