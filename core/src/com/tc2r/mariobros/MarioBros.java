package com.tc2r.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tc2r.mariobros.Screens.PlayScreen;

public class MarioBros extends Game {
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 1 << 1;
	public static final short BRICK_BIT = 1 << 2;
	public static final short COIN_BIT = 1 << 3;
	public static final short DESTROYED_BIT = 1 << 4;
	public static final short OBJECT_BIT = 1 << 5;
	public static final short ENEMY_BIT = 1 << 6;
	public static final short ENEMY_HEAD_BIT = 1 << 7;
	public static final short ITEM_BIT = 1 << 8;
	public static final short MARIO_HEAD_BIT = 1 << 9;
	public static final short MARIO_FEET_BIT = 1 << 10;
	public static final short FIREBALL_BIT = 1 << 12;



	public SpriteBatch batch;

//	Warning, Using AssetManager in a static way can cause issues in android.
	public static AssetManager manager;

//	public MarioBros() {
//
//		if (manager == null) {
//			manager = new AssetManager();
//			loadAudio();
//		}
//
//	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		if (manager == null) {
			manager = new AssetManager();
			loadAudio();
		}

		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();

	}

	private void loadAudio(){
		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.load("audio/sounds/powerdown.wav", Sound.class);
		manager.load("audio/sounds/stomp.wav", Sound.class);
		manager.load("audio/sounds/mariodie.wav", Sound.class);
		manager.finishLoading();
	}
}
