package com.tc2r.mariobros.Sprites.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Tc2r on 10/13/2017.
 * <p>
 * Description:
 */

public class ItemDef {
	public Vector2 position;
	public Class<?> type;

	public ItemDef(Vector2 position, Class<?> type) {
		this.position = position;
		this.type = type;
	}


}
