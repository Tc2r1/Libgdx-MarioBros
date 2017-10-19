package com.tc2r.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Scenes.Hud;
import com.tc2r.mariobros.Screens.PlayScreen;
import com.tc2r.mariobros.Sprites.Items.ItemDef;
import com.tc2r.mariobros.Sprites.Items.Mushroom;
import com.tc2r.mariobros.Sprites.Mario;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class Coin extends com.tc2r.mariobros.Sprites.TileObjects.InteractiveTileObject {

	private static TiledMapTileSet tileSet;
	private final int BLANK_COIN = 28;

	public Coin(PlayScreen screen, MapObject object) {
		super(screen, object);
		tileSet = map.getTileSets().getTileSet("tileset_gutter");
		fixture.setUserData(this);
		setCategoryFilter(MarioBros.COIN_BIT);



	}

	@Override
	public void onHeadHit(Mario mario) {
		Gdx.app.log("Hit", "Coin");
		if(getCell().getTile().getId() == BLANK_COIN){
			MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
		}else {

			if(object.getProperties().containsKey("mushroom")) {
				screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioBros.PPM),
								Mushroom.class));
				MarioBros.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
			} else{
				MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
			}

			getCell().setTile(tileSet.getTile(BLANK_COIN));
			Hud.addScore(100);
		}
	}
}
