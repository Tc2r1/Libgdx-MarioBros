package com.tc2r.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Screens.PlayScreen;
import com.tc2r.mariobros.Sprites.Enemies.Enemy;
import com.tc2r.mariobros.Sprites.Enemies.Turtle;
import com.tc2r.mariobros.Sprites.Other.FireBall;

import static com.tc2r.mariobros.Sprites.Mario.State.JUMPING;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class Mario extends Sprite {
	private State state;


	public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD};
	public State currentState, previousState;

	public World world;
	public Body b2body;
	private TextureRegion marioStand, marioJump, marioDead;
	private TextureRegion bigMarioStand, bigMarioJump;

	private Animation<TextureRegion> bigMarioRun, growMario, marioRun;

	private boolean runningRight, marioIsBig, runGrowAnimation, marioIsDead;
	private boolean timeToDefineBigMario;
	private boolean timeToRedefineMario;
	private float stateTimer;
	private PlayScreen screen;

	private static Array<FireBall> fireBalls;
	private FireBall fireball;




	public Mario(PlayScreen screen) {

		this.screen = screen;
		this.world = screen.getWorld();
		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		runningRight = true;

		Array<TextureRegion> frames = new Array<TextureRegion>();

		// run animation for mario.
		for (int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16,16));
			Gdx.app.log("frame coords", "x: " + (i * 16));
			marioRun = new Animation<TextureRegion>(0.1f, frames);

		}
		// clear frames for next animation sequence.
		frames.clear();

		// Run Animation for Big Mario.
		for (int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16,32));
			Gdx.app.log("frame coords", "x: " + (i * 16));
			bigMarioRun = new Animation<TextureRegion>(0.1f, frames);

		}
		// clear frames for next animation sequence.
		frames.clear();


		// get set animation frames from growing mario.
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16,32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16,32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16,32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16,32));
		growMario = new Animation<TextureRegion>(0.2f, frames);

		// Get jump animation frames and add them to marioJump animation.
		marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16,16);
		bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16,32);

		frames.clear();

		// create texture region for mario standing.
		marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
		bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32);

		// Create dead mario texture region
		marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16,16);

		// define mario in Box2D
		defineMario();
		setBounds(0,0, 16/ MarioBros.PPM, 16/MarioBros.PPM);
		setRegion(marioStand);

		fireBalls = new Array<FireBall>();
	}

	public boolean isBig() {
		return marioIsBig;
	}

	public boolean isDead() {
		return marioIsDead;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public void update(float delta) {

		// Time runs out, Mario dies.

		if (screen.getHud().isTimeUp() && !isDead()){
			killMario();
			fireBalls.clear();
		}

		if(marioIsBig){
			//update sprite to correspond with the position of Box2D body
			setPosition(b2body.getPosition().x - getWidth()/ 2, b2body.getPosition().y - getHeight()/2 - 5/MarioBros.PPM);

		} else {
			//update sprite to correspond with the position of Box2D body
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		}
		// update sprite with the correct frame depending on Mario's current action.
		setRegion(getFrame(delta));

		if (timeToDefineBigMario) {
			defineBigMario();
		}
		if (timeToRedefineMario) {
			redefineMario();
		}
		for (FireBall fireball : fireBalls) {
			if(fireball.isDestroyed()){
				fireBalls.removeValue(fireball, true);
				return;
			} else{
				fireball.update(delta);
			}
		}
	}


	public TextureRegion getFrame(float delta) {

		// Get mario's current State!
		currentState = getState();
		TextureRegion region;

		// Set animation keyframe by state.
		switch (currentState) {
			case DEAD:
				region = marioDead;
				break;
			case GROWING:
				region = growMario.getKeyFrame(stateTimer, false);
				if (growMario.isAnimationFinished(stateTimer)) {
					runGrowAnimation = false;
				}
				break;
			case JUMPING:
				region = marioIsBig ? bigMarioJump : marioJump;
				break;

			case RUNNING:
				region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true);
				break;

			case FALLING:
			case STANDING:
				default:
					region = marioIsBig ? bigMarioStand : marioStand;
					break;
		}

		if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
			region.flip(true, false);
			runningRight = false;

		} else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}
		stateTimer = currentState == previousState ? stateTimer + delta : 0;
		previousState = currentState;
		return region;
	}


	public State getState() {

		if(marioIsDead){
			return State.DEAD;

		} else if(runGrowAnimation) {
			return State.GROWING;

		} else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y > 0 && previousState == JUMPING)){
			return JUMPING;

		} else if(b2body.getLinearVelocity().y < 0){
			return State.FALLING;

		} else if(b2body.getLinearVelocity().x != 0) {

			return State.RUNNING;

		} else {
			return State.STANDING;
		}
	}

	public void redefineMario() {

		Vector2 position = b2body.getPosition();
		world.destroyBody(b2body);

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5/ MarioBros.PPM);
		fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
		fixtureDef.filter.maskBits =
						MarioBros.GROUND_BIT |
										MarioBros.COIN_BIT   |
										MarioBros.BRICK_BIT  |
										MarioBros.ENEMY_BIT  |
										MarioBros.OBJECT_BIT |
										MarioBros.ITEM_BIT   |
										MarioBros.ENEMY_HEAD_BIT;
		fixtureDef.shape = shape;
		b2body.createFixture(fixtureDef).setUserData(this);


		EdgeShape feet = new EdgeShape();
		feet.set(new Vector2(-2 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, -6 / MarioBros.PPM));
		fixtureDef.shape = feet;
		fixtureDef.filter.categoryBits = MarioBros.MARIO_FEET_BIT;
		b2body.createFixture(fixtureDef);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2/ MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2/ MarioBros.PPM, 6 / MarioBros.PPM));
		fixtureDef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
		fixtureDef.shape = head;
		fixtureDef.isSensor = true;
		b2body.createFixture(fixtureDef).setUserData(this);

		timeToRedefineMario = false;

		shape.dispose();
		head.dispose();
		feet.dispose();
	}


	public void defineBigMario() {
		Vector2 currentPosition = b2body.getPosition();
		world.destroyBody(b2body);


		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(currentPosition.add(0, 10/MarioBros.PPM));
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5/ MarioBros.PPM);
		fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
		fixtureDef.filter.maskBits =
						MarioBros.GROUND_BIT |
										MarioBros.COIN_BIT   |
										MarioBros.BRICK_BIT  |
										MarioBros.ENEMY_BIT  |
										MarioBros.OBJECT_BIT |
										MarioBros.ITEM_BIT   |
										MarioBros.ENEMY_HEAD_BIT;

		fixtureDef.shape = shape;
		b2body.createFixture(fixtureDef).setUserData(this);
		shape.setPosition(new Vector2(0,-14/MarioBros.PPM));
		b2body.createFixture(fixtureDef).setUserData(this);


		EdgeShape feet = new EdgeShape();
		feet.set(new Vector2(-2 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, -6 / MarioBros.PPM));
		fixtureDef.shape = feet;
		fixtureDef.filter.categoryBits = MarioBros.MARIO_FEET_BIT;
		b2body.createFixture(fixtureDef);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2/ MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2/ MarioBros.PPM, 6 / MarioBros.PPM));
		fixtureDef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
		fixtureDef.shape = head;
		fixtureDef.isSensor = true;
		b2body.createFixture(fixtureDef).setUserData(this);

		timeToDefineBigMario = false;
		shape.dispose();
		head.dispose();
		feet.dispose();
	}

	public void defineMario() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5/ MarioBros.PPM);
		fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
		fixtureDef.filter.maskBits =
						MarioBros.GROUND_BIT |
						MarioBros.COIN_BIT   |
						MarioBros.BRICK_BIT  |
						MarioBros.ENEMY_BIT  |
						MarioBros.OBJECT_BIT |
						MarioBros.ITEM_BIT   |
						MarioBros.ENEMY_HEAD_BIT;
		fixtureDef.shape = shape;
		b2body.createFixture(fixtureDef).setUserData(this);

		EdgeShape feet = new EdgeShape();
		feet.set(new Vector2(-2 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, -6 / MarioBros.PPM));
		fixtureDef.shape = feet;
		fixtureDef.filter.categoryBits = MarioBros.MARIO_FEET_BIT;
		b2body.createFixture(fixtureDef);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2/ MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2/ MarioBros.PPM, 6 / MarioBros.PPM));
		fixtureDef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
		fixtureDef.shape = head;
		fixtureDef.isSensor = true;
		b2body.createFixture(fixtureDef).setUserData(this);
		shape.dispose();
		head.dispose();
		feet.dispose();
	}

	public void grow() {
		runGrowAnimation = true;
		marioIsBig = true;
		timeToDefineBigMario = true;
		MarioBros.manager.get("audio/sounds/powerup.wav", Sound.class).play();
		setBounds(getX(), getY(), getWidth(), getHeight()*2);
	}

	public void hit(Enemy enemy){
		if(enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL){
			((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);

		} else{
			if(marioIsBig){
				marioIsBig = false;
				timeToRedefineMario = true;
				setBounds(getX(), getY(), getWidth(), getHeight()/2);
				MarioBros.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
			} else {
				killMario();
			}
		}
	}

	public void killMario() {
		if(!isDead()) {
			MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
			MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
			marioIsDead = true;
			Filter filter = new Filter();
			filter.maskBits = MarioBros.NOTHING_BIT;
			for (Fixture fixture :
							b2body.getFixtureList()) {
				fixture.setFilterData(filter);
				b2body.applyLinearImpulse(new Vector2(0, 2f), b2body.getWorldCenter(), true);

			}
		}
	}

	public void fire(){
		if (runningRight) {
			fireball = new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, true);
		} else {
			fireball = new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, false);
		}
		fireBalls.add(fireball);
	}

	public void draw(Batch batch) {
		super.draw(batch);
		for (FireBall fireball : fireBalls) {
			if(!fireball.isDestroyed()) {
				fireball.draw(batch);
			}
		}
	}

	public static void removeFireBall(FireBall fireBall) {
		fireBalls.removeValue(fireBall, true);
	}

}
