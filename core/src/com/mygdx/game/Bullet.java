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

    public Bullet(float x, float y, float radians, float speed) {

        this.x = x;
        this.y = y;
        this.radians = radians;

        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

        width = height = 2;

        lifeTimer = 0;
        lifeTime = 1;

    }

    public boolean shouldRemove() { return remove; }

    protected void wrap() {
        if(x < 0) x = game_width;
        if(x > game_width) x = 0;
        if(y < 0) y = game_height;
        if(y > game_height) y = 0;
    }

    public void update(float dt) {

        x += dx * dt;
        y += dy * dt;

        wrap();

        lifeTimer += dt;
        if(lifeTimer > lifeTime) {
            remove = true;
        }

    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x - width / 2, y - height / 2, width / 2);
        sr.end();
    }

}