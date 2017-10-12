package com.tc2r.mariobros.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.tc2r.mariobros.Sprites.InteractiveTileObject;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class WorldContactListener implements ContactListener {
	@Override
	public void beginContact(Contact contact) {
		Fixture fixa = contact.getFixtureA();
		Fixture fixb = contact.getFixtureB();

		if("head".equals(fixa.getUserData()) || "head".equals(fixb.getUserData())){
			Fixture head = "head".equals(fixa.getUserData())? fixa : fixb;

			Fixture object = head == fixa ? fixb : fixa;

			if (object.getUserData() != null && object.getUserData() instanceof InteractiveTileObject) {
				((InteractiveTileObject) object.getUserData()).onHeadHit();
			}
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
