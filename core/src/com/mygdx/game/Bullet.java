package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends GameObject {

    private float lifeTime;
    private float lifeTimer;

    private float game_width = Gdx.graphics.getWidth();
    private float game_height = Gdx.graphics.getHeight();

    private boolean remove;

    private float xPercent;
    private float yPercent;
    private double speed = 5;

    public Bullet(float x, float y, float knobPercentX, float knobPercentY) {

        this.x = x;
        this.y = y;
        this.xPercent = knobPercentX;
        this.yPercent = knobPercentY;
        this.speed = 1.5 * speed;


        width = height = 10;

//        lifeTimer = 0;
//        lifeTime = 500;

    }

    public boolean shouldRemove() { return this.x > game_width || this.y > game_height || this.x < 0 || this.y < 0; }

    protected void wrap() {
        if(x < 0) x = game_width;
        if(x > game_width) x = 0;
        if(y < 0) y = game_height;
        if(y > game_height) y = 0;
    }

    public void update(float dt) {

        x += xPercent * speed;
        y += yPercent * speed;

//        System.out.println("--------------------------------- X PERCENT: " + xPercent);
//        System.out.println("--------------------------------- Y PERCENT: " + yPercent);
//        System.out.println(speed);

        //wrap();

//        lifeTimer += dt;
//        if(lifeTimer > lifeTime) {
//            remove = true;
//        }

    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x - width / 2, y - height / 2, width / 2);
        sr.end();
    }

}