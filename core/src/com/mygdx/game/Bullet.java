package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Bullet extends GameObject {

    private final float GAME_WIDTH = Gdx.graphics.getWidth();
    private final float GAME_HEIGHT = Gdx.graphics.getHeight();

    private double xPercent;
    private double yPercent;
    private double speed = 10;
    private double angle;

    public Bullet(float x, float y, float knobPercentX, float knobPercentY) {

        this.x = x;
        this.y = y;

        //find angle so we can normalize speed of bullet
        this.angle = Math.atan2(knobPercentY, knobPercentX);

        this.yPercent = Math.sin(this.angle);
        this.xPercent = Math.cos(this.angle);

        width = height = 10;
    }

    public boolean shouldRemove() { return this.x > GAME_WIDTH || this.y > GAME_HEIGHT || this.x < 0 || this.y < 0; }

    protected void wrap() {
        if(x < 0) x = GAME_WIDTH;
        if(x > GAME_WIDTH) x = 0;
        if(y < 0) y = GAME_HEIGHT;
        if(y > GAME_HEIGHT) y = 0;
    }

    public void update(float dt) {
        x += xPercent * speed;
        y += yPercent * speed;
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x - width / 2, y - height / 2, width / 2);
        sr.end();
    }
}