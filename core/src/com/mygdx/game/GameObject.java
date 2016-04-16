package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class GameObject {

    protected float x;
    protected float y;

    protected float dx;
    protected float dy;

    protected float radians;
    protected float speed;
    protected float rotationSpeed;

    protected int width;
    protected int height;

    protected float[] shapex;
    protected float[] shapey;

    protected void wrap() {
        if(x < 0) x = Gdx.graphics.getWidth();
        if(x > Gdx.graphics.getWidth()) x = 0;
        if(y < 0) y = Gdx.graphics.getHeight();
        if(y > Gdx.graphics.getHeight()) y = 0;
    }

}
