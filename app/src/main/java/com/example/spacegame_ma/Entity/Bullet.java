package com.example.spacegame_ma.Entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Bullet implements Entity{

    int x,y;
    public int width = 50;
    public int height = 50;
    private int color;
    private Rect rect;
    public int speed = 1;

    public Bullet(int posX, int posY){

        color = Color.rgb(255, 175, 0);

        this.x = posX;
        this.y = posY;


        rect = new Rect(-width, -height, -2*width, -2*height);
    }

    @Override
    public Point move(){
        y -= speed;
        return new Point(x,y);
    }

    @Override
    public Rect collisionShape() {
        return rect;
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

}
