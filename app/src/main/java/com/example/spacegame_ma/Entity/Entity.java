package com.example.spacegame_ma.Entity;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public interface Entity {

    void draw(Canvas canvas);
    void update(Point point);

    Point move();
    Rect collisionShape();

}
