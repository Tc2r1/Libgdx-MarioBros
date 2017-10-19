package com.tc2r.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Scenes.Hud;
import com.tc2r.mariobros.Screens.PlayScreen;
import com.tc2r.mariobros.Sprites.Mario;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class Brick extends InteractiveTileObject {



	public Brick(PlayScreen screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(MarioBros.BRICK_BIT);
	}

	@Override
	public void onHeadHit(Mario mario) {

		if(mario.isBig()) {
			setCategoryFilter(MarioBros.DESTROYED_BIT);
			getCell().setTile(null);
			Hud.addScore(200);
			if (MarioBros.manager.isLoaded("audio/sounds/breakblock.wav")) {
				Gdx.app.log("SOUND", "LOADED");
				MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class);

			} else {
				Gdx.app.log("SOUND", "NOT LOADED");
			}
		} else {
			MarioBros.manager.get("audio/sounds/bump.wav", Sound.class);
		}
	}
}
