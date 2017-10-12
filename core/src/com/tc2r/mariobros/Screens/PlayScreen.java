package com.tc2r.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Scenes.Hud;
import com.tc2r.mariobros.Sprites.Mario;
import com.tc2r.mariobros.Tools.B2WorldCreator;
import com.tc2r.mariobros.Tools.WorldContactListener;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class PlayScreen implements Screen {

	//Reference to our Game, used to set Screens
	private MarioBros game;
	private TextureAtlas atlas;

	// Basic playscreen variables
	private OrthographicCamera gamecam;
	private Viewport viewport;
	private Hud hud;

	// Tiled Map Variables
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	// Box2d variables
	private World world;
	private Box2DDebugRenderer box2DDebugRenderer;

	// Sprites
	private Mario player; // Mario class object.

	private Music music;

	public PlayScreen(MarioBros game) {

		atlas = new TextureAtlas("Mario_and_Enemies.pack");

		this.game = game;

		// Create cam used to follow mario around the cam world.
		gamecam = new OrthographicCamera();

		// Create a Fitviewport to maintain virtual aspect ratio despite screen size.
		viewport = new FitViewport(MarioBros.V_WIDTH /MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);

		// Create the game HUD for scores/timers/level info
		hud = new Hud(game.batch);


		// Load map and setup the map renderer
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("level1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

		// initially set our gamecam to be centered correctly at the start of a map.
		gamecam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

		// Add the debug lines to our box2d world.
		box2DDebugRenderer = new Box2DDebugRenderer();


		//create the Box2D world, setting no gravity in x, -10 gravity in Y.
		world = new World(new Vector2(0, -10), true);


		new B2WorldCreator(world, map);

		player = new Mario(world, this);

		world.setContactListener(new WorldContactListener());

		music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
		music.setLooping(true);
		music.play();
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}
	@Override
	public void show() {

	}

	public void update(float delta) {
		handleInput(delta);

		world.step(1 / 60f, 6, 2);

		player.update(delta);
		hud.update(delta);

		// everytime mario moves, we track him.
		gamecam.position.x = player.b2body.getPosition().x;
		gamecam.update();

		//tell our renderer to draw only what our camera can see in our game world.
		renderer.setView(gamecam);





	}

	private void handleInput(float delta) {

		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			// Impulse is immediate change.
			player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(), true);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
						player.b2body.getLinearVelocity().x <= 2){
			player.b2body.applyLinearImpulse(new Vector2(0.1f, 0f), player.b2body.getWorldCenter(), true);

		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
						player.b2body.getLinearVelocity().x >= -2){
			player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0f), player.b2body.getWorldCenter(), true);

		}
	}



	@Override
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(1,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render our game map
		renderer.render();

		// renderer our Box2DDebugLines
		box2DDebugRenderer.render(world, gamecam.combined);

		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();



		// Set batch to draw what the hut camera sees.
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		// Dispose of all open resources.
		renderer.dispose();
		world.dispose();
		box2DDebugRenderer.dispose();
		hud.dispose();
		map.dispose();


	}
}
