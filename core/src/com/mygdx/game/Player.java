package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Player extends GameObject {

    private final int MAX_BULLETS = 4;
    public ArrayList<Bullet> bullets;

    private boolean up;

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

        //Create block sprite
        blockTexture = new Texture(Gdx.files.internal("block.png"));
        blockSprite = new Sprite(blockTexture);
        //Set position to centre of the screen
        blockSprite.setPosition(game_width / 2 - blockSprite.getWidth() / 2, game_height / 2 - blockSprite.getHeight() / 2);

        blockSpeed = 5;

        this.shapeRenderer = shapeRenderer;
    }

    public boolean shoot() {
        if(bullets.size() == MAX_BULLETS) {
            return false;
        } else {
            bullets.add(new Bullet(blockSprite.getX(), blockSprite.getY(), lastNonZeroDx, lastNonZeroDy));
            return true;
        }
    }

    public void update(float dt, float knobPercentX, float knobPercentY) {

        blockSprite.setX(blockSprite.getX() + knobPercentX * blockSpeed);
        blockSprite.setY(blockSprite.getY() + knobPercentY * blockSpeed);
        if (blockSprite.getX() < 0) {
            blockSprite.setX(0);
        } else if (blockSprite.getX() > game_width - blockTexture.getWidth()) {
            blockSprite.setX(game_width - blockTexture.getWidth());
        }
        if (blockSprite.getY() < 0) {
            blockSprite.setY(0);
        } else if (blockSprite.getY() > game_height - blockTexture.getHeight()) {
            blockSprite.setY(game_height - blockTexture.getHeight());
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
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(1, 1, 1, 1);
        sr.begin(ShapeType.Line);
        sr.end();
    }

}
