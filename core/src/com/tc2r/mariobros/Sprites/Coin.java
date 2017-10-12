package com.tc2r.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Scenes.Hud;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class Coin extends InteractiveTileObject{

	private static TiledMapTileSet tileSet;
	private final int BLANK_COIN = 28;

	public Coin(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		tileSet = map.getTileSets().getTileSet("tileset_gutter");
		fixture.setUserData(this);
		setCategoryFilter(MarioBros.COIN_BIT);



	}

	@Override
	public void onHeadHit() {
		Gdx.app.log("Hit", "Coin");
		if(getCell().getTile().getId() == BLANK_COIN){
			MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
		}else {
			MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
			getCell().setTile(tileSet.getTile(BLANK_COIN));
			Hud.addScore(100);
		}
	}
}