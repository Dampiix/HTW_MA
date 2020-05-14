package com.example.spacegame_ma.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.spacegame_ma.Entity.Player;
import com.example.spacegame_ma.Logic.Constants;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean isRunning;
    private Player player;
    //private Point playerPos;


    public GameView(Context context) {
        super(context);
        init(context);
        player = new Player(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        //playerPos = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);
        player.update(player.move());

    }

    public void init(Context context){
        Constants.CURRENT_CONTEXT = context;
        System.out.println(("init"));
    }

    @Override
    public void run() {

        while(isRunning){

            update();
            draw();
            sleep();

        }

    }

    public void update(){
        player.update(player.move());

    }


    public void draw(){
        if(getHolder().getSurface().isValid()){
            System.out.println(("is valid"));
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.WHITE);
            player.draw(canvas);
            getHolder().unlockCanvasAndPost(canvas);

        }else{
            System.out.println(("is not valid"));
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
}
