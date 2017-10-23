package com.tc2r.mariobros.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Screens.PlayScreen;
import com.tc2r.mariobros.Sprites.Mario;
import com.tc2r.mariobros.Tools.B2WorldCreator;

/**
 * Created by Tc2r on 10/13/2017.
 * <p>
 * Description:
 */

public class Goomba extends Enemy {

	private float stateTime;
	private Animation<TextureRegion> walkAnimation;
	private Array<TextureRegion> frames;
	private boolean setToDestroy;
	private boolean destroyed;
	private float angle;



	public Goomba(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		frames = new Array<TextureRegion>();
		for (int i = 0; i < 2; i++) {
			frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));

		}
		walkAnimation = new Animation(0.4f, frames);
		stateTime = 0;
		setBounds(getX(), getY(), 16/MarioBros.PPM, 16/MarioBros.PPM);
		setToDestroy = false;
		destroyed = false;
		angle = 0;
	}

	public void update(float delta) {
		stateTime += delta;
		if (setToDestroy && !destroyed) {
			world.destroyBody(b2body);
			destroyed = true;
			setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
			B2WorldCreator.removeEnemy(this);
			stateTime = 0;

		} else if(!destroyed) {
			b2body.setLinearVelocity(velocity);
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
			setRegion(walkAnimation.getKeyFrame(stateTime, true));
		}
	}
	@Override
	protected void defineEnemy() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(getX(), getY());
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6/ MarioBros.PPM);
		fixtureDef.filter.categoryBits = MarioBros.ENEMY_BIT;
		fixtureDef.filter.maskBits = MarioBros.GROUND_BIT |
						MarioBros.COIN_BIT |
						MarioBros.BRICK_BIT |
						MarioBros.ENEMY_BIT |
						MarioBros.MARIO_BIT |
						MarioBros.FIREBALL_BIT|
						MarioBros.OBJECT_BIT;

		fixtureDef.shape = shape;
		b2body.createFixture(fixtureDef).setUserData(this);

		// Create the head
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-5,8).scl(1/MarioBros.PPM);
		vertice[1] = new Vector2(5,8).scl(1/MarioBros.PPM);
		vertice[2] = new Vector2(-3,3).scl(1/MarioBros.PPM);
		vertice[3] = new Vector2(3,3).scl(1/MarioBros.PPM);
		head.set(vertice);

		fixtureDef.shape = head;
		fixtureDef.restitution = 0.7f;
		fixtureDef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
		b2body.createFixture(fixtureDef).setUserData(this);

		shape.dispose();
		head.dispose();
	}

	public void draw(Batch batch) {
		// Only draw if goomba is not destroyed
		// or the state time is less than 1 second.
		if (!destroyed || stateTime < 1) {
			super.draw(batch);
		}
	}

	@Override
	public void onEnemyHit(Enemy enemy) {
		if (enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL) {
			setToDestroy = true;

		} else {
			reverseVelocity(true, false);
		}

	}

	@Override
	public void hitOnHead(Mario mario) {
		setToDestroy = true;
		MarioBros.manager.get("audio/sounds/stomp.wav", Sound.class).play();

		Filter filter = new Filter();
		filter.maskBits = MarioBros.NOTHING_BIT;

		for (Fixture fixture : b2body.getFixtureList()) {
			fixture.setFilterData(filter);
		}

	}

	@Override
	public void hitByFireBall() {
		setToDestroy = true;
		// ADD SOUND EFFECT

	}

}
