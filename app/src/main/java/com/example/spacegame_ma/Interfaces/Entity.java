package com.example.spacegame_ma.Interfaces;

import android.graphics.Canvas;
import android.graphics.Point;

public interface Entity {

    void draw(Canvas canvas);
    void update();

    Point move();

}
