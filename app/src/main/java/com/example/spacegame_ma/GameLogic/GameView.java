package com.example.spacegame_ma.GameLogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

public class GameView extends SurfaceView implements Runnable, GameViewI {

    private Thread thread;
    private boolean isRunning;
    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private float enemySpeed = 2;
    private int max_reload_time = 10;
    private int reload_timer = max_reload_time;
    private int maxEnemyTimer = 60;
    private int enemyTimer;
    private Rect bulletShape;
    private int screenX, screenY;

    private Paint paint;

    public int score = 0;
    public int highscore;
    private boolean gameOver = false;

    private Random random;

    private SharedPreferences pref;

    public GameView(Context context) {
        super(context);
        init(context);
        spawnPlayer();

        pref = context.getSharedPreferences("game", Context.MODE_PRIVATE);

        screenX = Constants.SCREEN_WIDTH;
        screenY = Constants.SCREEN_HEIGHT;

        player.update(player.move());
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        random = new Random();

        enemyTimer = maxEnemyTimer;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);


    }


    public void init(Context context){
        Constants.CURRENT_CONTEXT = context;
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

    @Override
    public void update(){
        spawnEnemy();
        player.update(player.move());
        List<Bullet> offscreenBullets = new ArrayList<>();
        List<Enemy> offscreenEnemies = new ArrayList<>();

        if(gameOver){
            isRunning = false;
            saveNewHighscore();
        }

        try{
            //check for all bullets on screen
            for (Bullet bullet : bullets){
                bullet.update(bullet.move());
                bulletShape = new Rect(bullet.move().x-(bullet.width/2), bullet.move().y - (bullet.height/2), bullet.move().x+ (bullet.width/2),bullet.move().y+ (bullet.height/2));
                for(Enemy enemy : enemies){
                    //check if any enemy is hit by a bullet
                    if(enemyHit(bullet, enemy)){
                        enemy.move().y = Constants.SCREEN_HEIGHT +100;

                        offscreenBullets.add(bullet);
                    }
                }
                //when bullets go off the screen
                if(bullet.move().y < 0){
                    offscreenBullets.add(bullet);
                }
            }
            for(Bullet bullet : offscreenBullets){
                bullets.remove(bullet);
            }
        }catch(Exception e){
            System.out.println("ERROR: something went wrong, but i have no idea what");
        }

        try{
            //check for all enemies
            for (Enemy enemy : enemies){
                if(enemy.move().y > Constants.SCREEN_HEIGHT +10){
                    offscreenEnemies.add(enemy);
                }
                enemy.update((enemy.move()));
                //check if any enemy touches the player
                isGameOver(player, enemy);
            }
            for(Enemy enemy : offscreenEnemies){
                enemies.remove((enemy));
            }
        }catch(Exception e){
            System.out.println("ERROR: something went wrong, but i have no idea what");
        }


        offscreenBullets.clear();
        offscreenEnemies.clear();

        reload_timer--;
        enemyTimer--;
    }

    @Override
    public void saveNewHighscore() {
        if (pref.getInt("highscore", 0) < score){
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    //draw everything on
    @Override
    public void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.rgb(10,10,44));
            try{
                for (Bullet bullet : bullets){
                    bullet.draw(canvas);
                }
            }catch(Exception e){
                System.out.println("ERROR: something went wrong, but i have no idea what");
            }
            try{
                for(Enemy enemy : enemies){
                    enemy.draw(canvas);
                }
            }catch(Exception e){
                System.out.println("ERROR: something went wrong, but i have no idea what");
            }

            canvas.drawText(score + "", screenX/2f, screenY/10, paint);

            player.draw(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void sleep(){
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
        if(reload_timer <=0){
            newBullet();
            reload_timer = max_reload_time;
        }
        return true;
    }

    @Override
    public void newBullet(){
        Bullet bullet = new Bullet(player.move().x, player.move().y);
        bullets.add(bullet);
    }

    @Override
    public void spawnEnemy(){
        if (enemyTimer <= 0){
            Enemy enemy = new Enemy(enemySpeed);
            enemies.add(enemy);
            if(maxEnemyTimer>18){
                maxEnemyTimer--;
            }
            enemySpeed += 0.1f;
            enemyTimer = maxEnemyTimer;
        }
    }

    @Override
    public void spawnPlayer(){
        player = new Player(this);
    }

    @Override
    public boolean enemyHit(Bullet b, Enemy e){
        bulletShape = new Rect(b.move().x-25, b.move().y -25, b.move().x+25,b.move().y+25);
        if(Rect.intersects(bulletShape, e.collisionShape())){
            score++;
            return true;
        }else{
            return false;
        }
    }


    @Override
    public boolean isGameOver(Player p, Enemy e){
        //game-over if player hit by enemy
        if(Rect.intersects(p.collisionShape(),e.collisionShape())){
            gameOver = true;
        }
        return gameOver;
    }

    @Override
    public int getScore(){
        return score;
    }


}
