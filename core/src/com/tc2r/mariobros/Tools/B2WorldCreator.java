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
import com.badlogic.gdx.utils.Array;
import com.tc2r.mariobros.MarioBros;
import com.tc2r.mariobros.Screens.PlayScreen;
import com.tc2r.mariobros.Sprites.Enemies.Enemy;
import com.tc2r.mariobros.Sprites.Enemies.Goomba;
import com.tc2r.mariobros.Sprites.Enemies.Turtle;
import com.tc2r.mariobros.Sprites.Enemies.Wombo;
import com.tc2r.mariobros.Sprites.TileObjects.Brick;
import com.tc2r.mariobros.Sprites.TileObjects.Coin;

/**
 * Created by Tc2r on 10/8/2017.
 * <p>
 * Description:
 */

public class B2WorldCreator {

	private static Array<Goomba> goombas;
	private static Array<Wombo> wombos;
	private static Array<Turtle> turtles;

	private World world;
	private TiledMap map;


	public B2WorldCreator(PlayScreen screen){
		world = screen.getWorld();
		map = screen.getMap();

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
			fixtureDef.filter.categoryBits = MarioBros.OBJECT_BIT;
			body.createFixture(fixtureDef);
		}

		// Create coin bodies /fixtures
		for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {


			new Coin(screen, object);
		}

		for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {


			new Brick(screen, object);
		}

		// Create all goombas
		goombas = new Array<Goomba>();
		for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			goombas.add(new Goomba(screen, rect.getX()/MarioBros.PPM, rect.getY()/MarioBros.PPM));
		}

		// Create all Wombas
		wombos = new Array<Wombo>();
//		for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
//			Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//			wombos.add(new Wombo(screen, rect.getX()/MarioBros.PPM, rect.getY()/MarioBros.PPM));
//		}

		// Create all turtles
		turtles = new Array<Turtle>();
		for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			turtles.add(new Turtle(screen, rect.getX()/MarioBros.PPM, rect.getY()/MarioBros.PPM));
		}

	}
	public Array<Enemy> getEnemies(){
		Array<Enemy> enemies = new Array<Enemy>();
		enemies.addAll(goombas);
		enemies.addAll(wombos);
		enemies.addAll(turtles);
		return enemies;
	}

	public static void removeEnemy(Enemy enemy) {
		if(enemy instanceof Turtle){
			turtles.removeValue((Turtle) enemy, true);
		}
		if (enemy instanceof Goomba) {
			goombas.removeValue((Goomba) enemy, true);
		}
		if (enemy instanceof Wombo) {
			wombos.removeValue((Wombo) enemy, true);

		}
	}
	public static void removeTurtle(Turtle turtle){
		turtles.removeValue(turtle, true);
	}
}
