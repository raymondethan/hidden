package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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

    private float lastNonZeroDx = (float) .5;
    private float lastNonZeroDy = (float) .5;

    private ShapeRenderer shapeRenderer;

    double shiftx = 0;
    double shifty = 0;

    public Player(ArrayList<Bullet> bullets, ShapeRenderer shapeRenderer) {

        this.bullets = bullets;

//        x = game_width / 2;
//        y = game_height / 2;

        //Create block sprite
        blockTexture = new Texture(Gdx.files.internal("block.png"));
        blockSprite = new Sprite(blockTexture);
        //Set position to centre of the screen
        blockSprite.setPosition(Gdx.graphics.getWidth() / 2 - blockSprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - blockSprite.getHeight() / 2);

        blockSpeed = 5;

        this.shapeRenderer = shapeRenderer;
    }

    public void shoot() {
        if(bullets.size() == MAX_BULLETS) return;
        bullets.add(new Bullet(blockSprite.getX(), blockSprite.getY(), lastNonZeroDx, lastNonZeroDy));
    }

    public void update(float dt, float knobPercentX, float knobPercentY) {

        blockSprite.setX(blockSprite.getX() + knobPercentX * blockSpeed);
        blockSprite.setY(blockSprite.getY() + knobPercentY * blockSpeed);
        if (blockSprite.getX() < 0) {
            blockSprite.setX(0);
        } else if (blockSprite.getX() > Gdx.graphics.getWidth() - blockTexture.getWidth()) {
            blockSprite.setX(Gdx.graphics.getWidth() - blockTexture.getWidth());
        }
        if (blockSprite.getY() < 0) {
            blockSprite.setY(0);
        } else if (blockSprite.getY() > Gdx.graphics.getHeight() - blockTexture.getHeight()) {
            blockSprite.setY(Gdx.graphics.getHeight() - blockTexture.getHeight());
        }

        if (0 != knobPercentX && 0 != knobPercentY) {
            lastNonZeroDx = knobPercentX;
            lastNonZeroDy = knobPercentY;
        }
        if (Math.random() < .025) {
            shiftx = Math.random() * 120 - 60;
            shifty = Math.random() * 120 - 60;
        }

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle((float) (blockSprite.getX() + shiftx), (float) (blockSprite.getY() + shifty), 200);
        shapeRenderer.end();

        // screen wrap
        //wrap();

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
