package com.tc2r.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Screens.PlayScreen;

import static com.tc2r.mariobros.Sprites.Mario.State.JUMPING;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class Mario extends Sprite {
	private State state;


	public enum State { FALLING, JUMPING, STANDING, RUNNING};
	public State currentState, previousState;

	public World world;
	public Body b2body;
	private TextureRegion marioStand;
	private Animation<TextureRegion> marioRun, marioJump;
	private boolean runningRight;
	private float stateTimer;




	public Mario(World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("little_mario"));
		this.world = world;

		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		runningRight = true;

		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 1; i < 4; i++) {

			frames.add(new TextureRegion(getTexture(), i * 16, 11, 16,16));
			Gdx.app.log("frame coords", "x: " + (i * 16));
			marioRun = new Animation<TextureRegion>(0.1f, frames);

		}
		frames.clear();
		for(int i = 4; i < 6; i++){
			frames.add(new TextureRegion(getTexture(), i*16, 11, 16, 16));
			marioJump = new Animation<TextureRegion>(0.1f, frames);

		}
		frames.clear();

		marioStand = new TextureRegion(getTexture(), 1, 11, 16, 16);

		defineMario();
		setBounds(0,0, 16/ MarioBros.PPM, 16/MarioBros.PPM);
		setRegion(marioStand);
	}

	public void update(float delta) {

		setPosition(b2body.getPosition().x - getWidth()/ 2, b2body.getPosition().y - getHeight()/2);
		setRegion(getFrame(delta));

	}

	public TextureRegion getFrame(float delta) {

		currentState = getState();
		TextureRegion region;
		switch (currentState) {
			case JUMPING:
				region = marioJump.getKeyFrame(stateTimer);
				break;

			case RUNNING:
				region = marioRun.getKeyFrame(stateTimer, true);
				break;

			case FALLING:
			case STANDING:
				default:
					region = marioStand;
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

		if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y > 0 && previousState == JUMPING)){
			return JUMPING;

		} else if(b2body.getLinearVelocity().y < 0){
			return State.FALLING;

		} else if (b2body.getLinearVelocity().x != 0) {

			return State.RUNNING;

		} else {
			return State.STANDING;
		}
	}

	private void defineMario() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5/ MarioBros.PPM);
		fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
		fixtureDef.filter.maskBits = MarioBros.DEFAULT_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT;
		fixtureDef.shape = shape;
		b2body.createFixture(fixtureDef);


		EdgeShape feet = new EdgeShape();
		feet.set(new Vector2(-2 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, -6 / MarioBros.PPM));
		fixtureDef.shape = feet;
		b2body.createFixture(fixtureDef);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2/ MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2/ MarioBros.PPM, 6 / MarioBros.PPM));
		fixtureDef.shape = head;
		fixtureDef.isSensor = true;
		b2body.createFixture(fixtureDef).setUserData("head");

	}

}
