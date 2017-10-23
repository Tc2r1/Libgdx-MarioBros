package com.tc2r.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.tc2r.mariobros.Screens.PlayScreen;
import com.tc2r.mariobros.Sprites.Mario;

/**
 * Created by Tc2r on 10/13/2017.
 * <p>
 * Description:
 */

public abstract class Enemy extends Sprite {
	protected World world;
	protected PlayScreen screen;
	public Body b2body;
	public Vector2 velocity;


	public Enemy(PlayScreen screen, float x, float y){
		this.world = screen.getWorld();
		this.screen = screen;
		setPosition(x, y);
		defineEnemy();
		velocity = new Vector2(-1, -2);
		b2body.setActive(false);
	}

	protected abstract  void defineEnemy();
	public abstract void onEnemyHit(Enemy enemy);
	public abstract void update(float delta);
	public abstract  void hitOnHead(Mario mario);
	public abstract  void hitByFireBall();

	public void reverseVelocity(Boolean x, Boolean y) {
		if(x){
			velocity.x = -velocity.x;
		}
		if(y){
			velocity.y = -velocity.y;
		}
	}


}
