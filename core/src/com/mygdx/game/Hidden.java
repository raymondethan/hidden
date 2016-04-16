package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Player;
import com.mygdx.game.Bullet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


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

    private String ipAddress;
    private ArrayList<Player> players = new ArrayList<Player>();

    SocketHints socketHints = new SocketHints();

    //Socket socket;

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
        stage.addActor(touchpad);
        stage.addActor(fireBtn);
        Gdx.input.setInputProcessor(stage);

        //Create block sprite
        final ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        player = new Player(bullets, shapeRenderer);
        players.add(player);

        //Play music
        bgMusic.setLooping(true);
        //bgMusic.play();

        // The following code loops through the available network interfaces
        // Keep in mind, there can be multiple interfaces per device, for example
        // one per NIC, one per active wireless and the loopback
        // In this case we only care about IPv4 address ( x.x.x.x format )
        List<String> addresses = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface ni : Collections.list(interfaces)){
                for(InetAddress address : Collections.list(ni.getInetAddresses()))
                {
                    if(address instanceof Inet4Address){
                        addresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // Print the contents of our array to a string.  Yeah, should have used StringBuilder
        for(String str:addresses)
        {
            if (str.startsWith("10")) {
                this.ipAddress = str;
            }
        }
        System.out.println("adddr: " + ipAddress);

        // Socket will time our in 4 seconds
        socketHints.connectTimeout = 4000;

        // Now we create a thread that will listen for incoming socket connections
        new Thread(new Runnable(){

            @Override
            public void run() {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // 0 means no timeout.  Probably not the greatest idea in production!
                serverSocketHint.acceptTimeout = 0;

                // Create the socket server using TCP protocol and listening on 9021
                // Only one app can listen to a port at a time, keep in mind many ports are reserved
                // especially in the lower numbers ( like 21, 80, etc )
                ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9021, serverSocketHint);

                // Loop forever
                while(true){
                    // Create a socket
                    Socket socket = serverSocket.accept(null);

                    // Read data from the socket into a BufferedReader
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    if (null != buffer) {

                        try {
                            // Read to the next newline (\n) and display that text on labelMessage
                            System.out.println("butter: " + buffer);
                            String[] data = buffer.readLine().split(",");
                            System.out.println("data" + data.toString());
                            System.out.println(Float.parseFloat(data[0]));
                            System.out.println(Float.parseFloat(data[1]));
                            if (players.size() == 1) {
                                Player p = new Player(new ArrayList<Bullet>(), shapeRenderer);
                                System.out.println("Player: " + p);
                                p.setPosition(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
                                players.add(p);
                            } else {
                                players.get(1).setPosition(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start(); // And, start the thread running

        //create the socket and connect to the server entered in the text box ( x.x.x.x format ) on port 9021
//        if (!this.ipAddress.equals("10.89.37.19")) {
//            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "10.89.37.19", 9021, socketHints);
//        }

	}

    @Override
    public void render() {

        Gdx.gl.glClearColor(0.294f, 0.294f, 0.294f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        //Move blockSprite with TouchPad
        player.update(1, touchpad.getKnobPercentX(), touchpad.getKnobPercentY());


        if (fireBtn.isPressed()) {
            player.shoot();
            shootSound.play();
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

            //Draw
            batch.begin();
            for(Player p : players) {
                p.draw(batch);
            }
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
        if (!"10.89.37.19".equals(this.ipAddress)) {

            Socket socket = Gdx.net.newClientSocket(Net.Protocol.TCP, "10.89.37.19", 9021, socketHints);
            System.out.println(player.blockSprite.getX());
            System.out.println(player.blockSprite.getY());

            try {
                // write our entered message to the stream
                socket.getOutputStream().write((player.blockSprite.getX() + "," + player.blockSprite.getY()).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
