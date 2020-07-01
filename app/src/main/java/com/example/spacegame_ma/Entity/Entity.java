package com.example.spacegame_ma.Entity;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public interface Entity {


    /**
    draws the entity objects

     */
    void draw(Canvas canvas);

    /**
    updates the position
     */
    void update(Point point);

    /**
    @return the position of an entity object
     */
    Point move();

    /**
    creates the collision shape for the entity objects
     @return a Rect object
     */
    Rect collisionShape();

}
