package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;


public class Hidden extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ShapeRenderer shapeRenderer;
	Touchpad touchpad;
    private OrthographicCamera camera;
    private Stage stage;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin fireSkin;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Player player;
    private TextureAtlas buttonAtlas;

    //fire button
    private ImageButton fireBtn;
    private ImageButton.ImageButtonStyle fireBtnStyle;

    private Music bgMusic;
    private Sound shootSound;
    private Sound damageSound;
    private Sound hitSound;

    private boolean start = true;
    private Texture ttrSplash;

	@Override
	public void create () {
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("mus_bg.ogg"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("mus_lazer.ogg"));
        damageSound = Gdx.audio.newSound(Gdx.files.internal("mus_damage.ogg"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("mus_hit.ogg"));

        shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();

        ttrSplash = new Texture("splash-screen.png");

        //Create camera
        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 10f * aspectRatio, 10f);

        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = (Drawable) touchpadSkin.getDrawable("touchBackground");
        touchKnob = (Drawable) touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = (com.badlogic.gdx.scenes.scene2d.utils.Drawable) touchBackground;
        touchpadStyle.knob = (com.badlogic.gdx.scenes.scene2d.utils.Drawable) touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 250, 250);

        //Fire button
        fireSkin = new Skin();
        //fireSkin.add("fireButton", new Texture("btnunpressed.png"));
        buttonAtlas = new TextureAtlas(Gdx.files.internal("fire.atlas"));
        fireSkin.addRegions(buttonAtlas);
        fireBtnStyle = new ImageButton.ImageButtonStyle();
        fireBtnStyle.up = fireSkin.getDrawable("btnunpressed");
        fireBtnStyle.down = fireSkin.getDrawable("btnpressed");
        fireBtn = new ImageButton(fireBtnStyle);
        fireBtn.setBounds(Gdx.graphics.getWidth() - 150, 50, 100, 100);

        //Create a Stage and add TouchPad
        stage = new Stage();
        //Use batch????????
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(stage);

        //Create block sprite
        ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        player = new Player(bullets, shapeRenderer);

        //Play music
        bgMusic.setLooping(true);
        //bgMusic.play();
	}

    @Override
    public void render() {

        Gdx.gl.glClearColor(0.294f, 0.294f, 0.294f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        //Move blockSprite with TouchPad
        player.update(1, touchpad.getKnobPercentX(), touchpad.getKnobPercentY());

        if (fireBtn.isPressed()) {
            if (player.shoot()) {
                shootSound.play();
            }
        }

        if (start) {
            //Draw splash screen
            batch = new SpriteBatch();
            batch.begin();
            batch.draw(ttrSplash, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
            stage.draw();
            if (Gdx.input.isTouched()) {
                start = false;
            }
        } else {
            //Add actors
            stage.addActor(touchpad);
            stage.addActor(fireBtn);
            //Draw
            batch.begin();
            player.blockSprite.draw(batch);
            batch.end();
            // update player bullets
            for (int i = 0; i < player.bullets.size(); i++) {
                player.bullets.get(i).update(1);
                player.bullets.get(i).draw(shapeRenderer);
                if (player.bullets.get(i).shouldRemove()) {
                    player.bullets.remove(i);
                    i--;
                }
            }
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();

        }
    }

    @Override
    public void dispose() {
        bgMusic.dispose();
        shootSound.dispose();
        hitSound.dispose();
        damageSound.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
    }

}
