package com.example.spacegame_ma.Entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.spacegame_ma.Controls.SensorDataInput;
import com.example.spacegame_ma.Interfaces.Entity;
import com.example.spacegame_ma.Logic.Constants;

public class Player implements Entity {

    int x,y;
    private int color;
    private Rect rect;
    private Point playerPosition = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);;

    private SensorDataInput orientationData;
    private long current_time;

    //shaky hand factor
    private int shfX = 5;
    private int shfY = 5;

    //
    private float sfX = 1000f;
    private float sfY = 1000f;

    public Player(Rect rect, int color){
        this.rect = rect;
        this.color = color;

        orientationData = new SensorDataInput();
        orientationData.register();
        current_time = System.currentTimeMillis();

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
        float oldLeft = rect.left;

        rect.set(
                point.x - rect.width() / 2,
                point.y - rect.height() / 2,
                point.x + rect.width() / 2,
                point.y + rect.height() / 2
        );

        int state = 0;
        if (rect.left - oldLeft > 5) {
            state = 1;
        } else if (rect.left - oldLeft < -5) {
            state = 2;
        }
    }

    public Point move(){
        if(current_time < Constants.INIT_TIME){
            current_time = Constants.INIT_TIME;
        }
        int passedTime = (int) (System.currentTimeMillis() - current_time);
        current_time = System.currentTimeMillis();

        if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null){

            float xOrientation = orientationData.getOrientation()[2];
            float xStartOrientation = orientationData.getStartOrientation()[2];

            float yOrientation = orientationData.getOrientation()[1];
            float yStartOrientation = orientationData.getStartOrientation()[1];

            float roll = (xOrientation - xStartOrientation);
            float pitch = (yOrientation - yStartOrientation);

            float speedX = 2* roll * Constants.SCREEN_WIDTH/sfX;
            float speedY = pitch * Constants.SCREEN_HEIGHT/sfY;


            //if start = display up
            if (xStartOrientation > -1.5 && xStartOrientation < 1.5){
                //  if current = display up
                if(xOrientation > -1.5 && xOrientation < 1.5){

                }
                //else if current = display down
                else if(xOrientation <= -1.5 || xOrientation >= 1.5){
                    float absOrientationY = Math.abs(yOrientation);
                    int posORneg = (int) (yOrientation/absOrientationY)* (-1) * 3;
                    yOrientation = (yOrientation + posORneg) * (-1);

                    if (xOrientation < 0){
                        xOrientation += 3;
                    }else if(xOrientation > 0){
                        xOrientation -= 3;
                    }
                    roll = (xOrientation - xStartOrientation);
                    pitch = (yOrientation - yStartOrientation);
                    speedX = roll * Constants.SCREEN_WIDTH/sfX;
                    speedY = pitch * Constants.SCREEN_HEIGHT/sfY;
                }
                playerPosition.y -= Math.abs(speedY * passedTime) > shfY ? speedY * passedTime : 0;
            }
            //else if start = display down
            else if (xStartOrientation <= -1.5 || xStartOrientation >= 1.5){
                //  if current = display up
                if(xOrientation > -1.5 && xOrientation < 1.5){
                    playerPosition.y -= Math.abs(speedY * passedTime) > shfY ? speedY * passedTime : 0;
                }
                //  else if current = display down
                else if(xOrientation <= -1.5 || xOrientation >= 1.5){
                    if (xStartOrientation <= -1.5 && xOrientation >= 1.5){
                        xOrientation -= 6;
                    }else if(xStartOrientation >= 1.5 && xOrientation <= -1.5){
                        xOrientation += 6;
                    }
                    roll = (xOrientation - xStartOrientation);
                    speedX = roll * Constants.SCREEN_WIDTH / sfX;

                    playerPosition.y += Math.abs(speedY * passedTime) > shfY ? speedY * passedTime : 0;
                }
            }
            playerPosition.x += Math.abs(speedX * passedTime) > shfX ? speedX * passedTime : 0;

            System.out.println("X: "+xOrientation + " Y: "+yOrientation);
        }
        if(playerPosition.x < 0){
            playerPosition.x = 0;
        }else if(playerPosition.x > Constants.SCREEN_WIDTH){
            playerPosition.x = Constants.SCREEN_WIDTH;
        }
        if(playerPosition.y < (0.7*Constants.SCREEN_HEIGHT)){
            playerPosition.y = (int) (0.7*Constants.SCREEN_HEIGHT);
        }else if(playerPosition.y > Constants.SCREEN_HEIGHT){
            playerPosition.y = (int) Constants.SCREEN_HEIGHT;
        }

        return playerPosition;
    }



}
