package com.tc2r.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Scenes.Hud;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class Brick extends InteractiveTileObject {



	public Brick(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		fixture.setUserData(this);
		setCategoryFilter(MarioBros.BRICK_BIT);
	}

	@Override
	public void onHeadHit() {

		setCategoryFilter(MarioBros.DESTROYED_BIT);
		getCell().setTile(null);
		Hud.addScore(200);
		if(MarioBros.manager.isLoaded("audio/sounds/breakblock.wav")) {
			Gdx.app.log("SOUND", "LOADED");
			MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class);

		}else{
			Gdx.app.log("SOUND", "NOT LOADED");
		}
	}
}
