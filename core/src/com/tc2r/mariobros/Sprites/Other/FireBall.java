package com.tc2r.mariobros.Sprites.Other;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Screens.PlayScreen;
import com.tc2r.mariobros.Sprites.Enemies.Enemy;
import com.tc2r.mariobros.Sprites.Enemies.Turtle;
import com.tc2r.mariobros.Sprites.Mario;

/**
 * Created by Tc2r on 10/22/2017.
 * <p>
 * Description:
 */
public class FireBall extends Sprite{

	PlayScreen screen;
	World world;
	TextureRegion fireBallTextureRegion;
	Array<TextureRegion> frames;
	Animation<TextureRegion> fireAnimation;
	float stateTime;
	boolean destroyed;
	boolean setToDestroy;
	boolean fireRight;
	Body b2body;

	public FireBall(PlayScreen screen, float x, float y, boolean fireRight) {
		this.fireRight = fireRight;
		this.screen = screen;
		this.world = screen.getWorld();
		stateTime = 0;
		frames = new Array<TextureRegion>();

		for(int i = 0; i < 4 ; i++) {
			frames.add(new TextureRegion(screen.getAtlas().findRegion("fireball"), i * 8, 0, 8, 8));
		}
		fireAnimation = new Animation<TextureRegion>(0.2f, frames);
		setRegion(fireAnimation.getKeyFrame(0));
		setBounds(x, y, 6 / MarioBros.PPM, 6 / MarioBros.PPM);
		defineFireBall();

	}

	public void defineFireBall() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(fireRight ? getX() + 12 / MarioBros.PPM : getX() - 12 / MarioBros.PPM, getY());
		bodyDef.type = BodyDef.BodyType.DynamicBody;

		if(!world.isLocked())
		b2body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(3/ MarioBros.PPM);
		fixtureDef.filter.categoryBits = MarioBros.FIREBALL_BIT;
		fixtureDef.filter.maskBits =
										MarioBros.GROUND_BIT |
										MarioBros.COIN_BIT   |
										MarioBros.BRICK_BIT  |
										MarioBros.ENEMY_BIT  |
										MarioBros.OBJECT_BIT |
										MarioBros.ITEM_BIT   ;

		fixtureDef.shape = shape;
		fixtureDef.restitution = 1;
		fixtureDef.friction = 0;

		b2body.createFixture(fixtureDef).setUserData(this);
		b2body.setLinearVelocity(new Vector2(fireRight ? 2 : -2, 2.5f));

	}

	public void update(float delta) {

 		stateTime += delta;

		if (world.isLocked()) {
			return;
		}
		if ((stateTime > 2 || setToDestroy) && !destroyed) {
			Mario.removeFireBall(this);
			b2body.setUserData(null);
			world.destroyBody(b2body);
			b2body = null;
			destroyed = true;
			return;
		}

		setRegion(fireAnimation.getKeyFrame(stateTime, true));
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

		if(b2body.getLinearVelocity().y > 2f){
			b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
		}
		if ((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0)) {
			setToDestroy();
		}
	}

	public void setToDestroy() {
		setToDestroy = true;
	}

	public boolean isDestroyed(){
		return destroyed;

	}

	public void hit(Enemy enemy) {
		if(enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL){
			((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);

		} else if(enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.MOVING_SHELL) {
			((Turtle)enemy).currentState = Turtle.State.STANDING_SHELL;
		} else{
			enemy.hitByFireBall();
		}
		setToDestroy();
	}
}
