package com.example.spacegame_ma.Entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.spacegame_ma.Constants.Constants;

import java.util.Random;

public class Enemy implements Entity {

    int x,y;
    int width = 100;
    int height = 100;
    private Rect rect;
    private int color;
    private float speed;

    private Point enemyPosition;
    private Random random;


    public Enemy(float speed){

        color = Color.rgb(255, 0, 0);
        this.speed = speed;
        random = new Random();
        x = random.nextInt(Constants.SCREEN_WIDTH);
        y =  0 - ((int)0.2*Constants.SCREEN_HEIGHT);
        enemyPosition = new Point(x,y);

        rect = new Rect(-width, -height, -2*width, -2*height);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rect, paint);
    }

    @Override
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

    @Override
    public Point move() {
        enemyPosition.y += speed;
        return enemyPosition;
    }

    @Override
    public Rect collisionShape() {
        return new Rect(enemyPosition.x - (width/2), enemyPosition.y -(height/2), enemyPosition.x + (width/2), enemyPosition.y + (height/2));
    }
}
