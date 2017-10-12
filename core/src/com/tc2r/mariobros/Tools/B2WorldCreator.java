package com.tc2r.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Sprites.Brick;
import com.tc2r.mariobros.Sprites.Coin;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class B2WorldCreator {

	private World world;
	private TiledMap map;


	public B2WorldCreator(World world, TiledMap map) {
		BodyDef bodyDef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fixtureDef = new FixtureDef();
		Body body;

		for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bodyDef.type = BodyDef.BodyType.StaticBody;
			bodyDef.position.set((rect.getX() + rect.getWidth() / 2)/ MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

			body = world.createBody(bodyDef);

			shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
			fixtureDef.shape = shape;
			body.createFixture(fixtureDef);
		}

		for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bodyDef.type = BodyDef.BodyType.StaticBody;
			bodyDef.position.set((rect.getX() + rect.getWidth() / 2)/ MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

			body = world.createBody(bodyDef);

			shape.setAsBox(rect.getWidth() / 2 / MarioBros.PPM, rect.getHeight() / 2 / MarioBros.PPM);
			fixtureDef.shape = shape;
			body.createFixture(fixtureDef);
		}

		for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			new Coin(world, map, rect);
		}

		for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			new Brick(world, map, rect);
		}

	}
}
