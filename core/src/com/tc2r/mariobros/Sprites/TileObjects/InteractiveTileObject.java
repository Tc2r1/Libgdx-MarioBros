package com.tc2r.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Screens.PlayScreen;
import com.tc2r.mariobros.Sprites.Mario;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public abstract class InteractiveTileObject {
	protected World world;
	protected PlayScreen screen;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Rectangle bounds;
	protected Body body;
	protected Fixture fixture;
	protected MapObject object;

	public InteractiveTileObject(PlayScreen screen, MapObject object) {
		this.object = object;
		this.screen = screen;
		this.world = screen.getWorld();
		this.map = screen.getMap();
		this.bounds = ((RectangleMapObject) object).getRectangle();

		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set((bounds.getX() + bounds.getWidth() / 2)/ MarioBros.PPM, (bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM);

		body = world.createBody(bodyDef);

		shape.setAsBox(bounds.getWidth() / 2 / MarioBros.PPM, bounds.getHeight() / 2 / MarioBros.PPM);
		fixtureDef.shape = shape;
		fixture = body.createFixture(fixtureDef);
	}

	public abstract void onHeadHit(Mario mario);

	public void setCategoryFilter(Short filterBit){
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);

	}

	public TiledMapTileLayer.Cell getCell(){
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		return layer.getCell((int) (body.getPosition().x * MarioBros.PPM / 16),
						(int) (body.getPosition().y * MarioBros.PPM / 16));

	}
}

