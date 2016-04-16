package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Player extends GameObject {

    private final int MAX_BULLETS = 4;
    public ArrayList<Bullet> bullets;

    private float[] flamex;
    private float[] flamey;

    private boolean left;
    private boolean right;
    private boolean up;

    private float maxSpeed;
    private float acceleration;
    private float deceleration;
    private float acceleratingTimer;

    private float game_width = Gdx.graphics.getWidth();
    private float game_height = Gdx.graphics.getHeight();

    public Texture blockTexture;
    public Sprite blockSprite;
    public float blockSpeed;

    public Player(ArrayList<Bullet> bullets) {

        this.bullets = bullets;

        x = game_width / 2;
        y = game_height / 2;

//        maxSpeed = 300;
//        acceleration = 200;
//        deceleration = 10;
//
//        shapex = new float[4];
//        shapey = new float[4];
//        flamex = new float[3];
//        flamey = new float[3];
//
//        radians = 3.1415f / 2;
//        rotationSpeed = 3;

        //Create block sprite
        blockTexture = new Texture(Gdx.files.internal("block.png"));
        blockSprite = new Sprite(blockTexture);
        //Set position to centre of the screen
        blockSprite.setPosition(Gdx.graphics.getWidth()/2-blockSprite.getWidth()/2, Gdx.graphics.getHeight()/2-blockSprite.getHeight()/2);

        blockSpeed = 5;

    }

    private void setShape() {
        shapex[0] = x + MathUtils.cos(radians) * 8;
        shapey[0] = y + MathUtils.sin(radians) * 8;

        shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * 8;
        shapey[1] = y + MathUtils.sin(radians - 4 * 3.1415f / 5) * 8;

        shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
        shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 5;

        shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f / 5) * 8;
        shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f / 5) * 8;
    }

    private void setFlame() {
        flamex[0] = x + MathUtils.cos(radians - 5 * 3.1415f / 6) * 5;
        flamey[0] = y + MathUtils.sin(radians - 5 * 3.1415f / 6) * 5;

        flamex[1] = x + MathUtils.cos(radians - 3.1415f) *
                (6 + acceleratingTimer * 50);
        flamey[1] = y + MathUtils.sin(radians - 3.1415f) *
                (6 + acceleratingTimer * 50);

        flamex[2] = x + MathUtils.cos(radians + 5 * 3.1415f / 6) * 5;
        flamey[2] = y + MathUtils.sin(radians + 5 * 3.1415f / 6) * 5;
    }

    public void setLeft(boolean b) { left = b; }
    public void setRight(boolean b) { right = b; }
    public void setUp(boolean b) { up = b; }

    public void shoot() {
        if(bullets.size() == MAX_BULLETS) return;
        bullets.add(new Bullet(x, y, radians, blockSpeed));
    }

    public void update(float dt) {

        // turning
        if(left) {
            radians += rotationSpeed * dt;
        }
        else if(right) {
            radians -= rotationSpeed * dt;
        }

        // accelerating
        if(up) {
            dx += MathUtils.cos(radians) * acceleration * dt;
            dy += MathUtils.sin(radians) * acceleration * dt;
            acceleratingTimer += dt;
            if(acceleratingTimer > 0.1f) {
                acceleratingTimer = 0;
            }
        }
        else {
            acceleratingTimer = 0;
        }

        // deceleration
        float vec = (float) Math.sqrt(dx * dx + dy * dy);
        if(vec > 0) {
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
        if(vec > maxSpeed) {
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }

        // set position
        x += dx * dt;
        y += dy * dt;

        // set shape
        setShape();

        // set flame
        if(up) {
            setFlame();
        }

        // screen wrap
        wrap();

    }

    public void draw(ShapeRenderer sr) {

        sr.setColor(1, 1, 1, 1);

        sr.begin(ShapeType.Line);

        // draw ship
        for(int i = 0, j = shapex.length - 1;
            i < shapex.length;
            j = i++) {

            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);

        }

        // draw flames
        if(up) {
            for(int i = 0, j = flamex.length - 1;
                i < flamex.length;
                j = i++) {

                sr.line(flamex[i], flamey[i], flamex[j], flamey[j]);

            }
        }


        sr.end();

    }

}
