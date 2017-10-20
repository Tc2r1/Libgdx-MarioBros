package com.tc2r.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Sprites.Enemies.Enemy;
import com.tc2r.mariobros.Sprites.Items.Item;
import com.tc2r.mariobros.Sprites.Mario;
import com.tc2r.mariobros.Sprites.TileObjects.InteractiveTileObject;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class WorldContactListener implements ContactListener {
	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
			case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
			case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
				if(fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT)
					((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
				else
					((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
				break;

			// Mario Stomps enemy Head
			case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:

				if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT) {
					// fixA is the enemy
					((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
				} else {
					// fixB is the enemy
					((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
				}
				break;

			// Enemy Collides with Another Enemy
			case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
				((Enemy) fixA.getUserData()).reverseVelocity(true, false);
				((Enemy) fixB.getUserData()).reverseVelocity(true, false);
				break;

			// Enemy Collides with an Object (Pipe, wall, etc)
			case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT) {
					// fixA is the enemy
					((Enemy)fixA.getUserData()).reverseVelocity(true, false);
				} else {
					// fixB is the enemy
					((Enemy) fixB.getUserData()).reverseVelocity(true, false);
				}
				break;

			// Mario Collides with an Enemy
			case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
				if(fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT)
					Gdx.app.log("testing", ((Enemy)fixB.getUserData()));
					((Mario) fixA.getUserData()).hit((Enemy)fixB.getUserData());

				if(fixB.getFilterData().categoryBits == MarioBros.MARIO_BIT)
					((Mario) fixB.getUserData()).hit((Enemy)fixA.getUserData());
				break;




			// Item Collides with an Object
			case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT) {
					// fixA is an Item
					((Item)fixA.getUserData()).reverseVelocity(true, false);
				} else {
					// fixB is an Item
					((Item) fixB.getUserData()).reverseVelocity(true, false);
				}
				break;

			// Item Mario Collides with Item.
			case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT) {
					// fixA is an Item
					((Item) fixA.getUserData()).use((Mario)fixB.getUserData());
				} else {
					// fixB is an Item
					((Item) fixB.getUserData()).use((Mario)fixA.getUserData());
				}
				break;


		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
