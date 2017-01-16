package model;

import model.game.Snake;
import view.MapDrawer;
import ch.judos.generic.data.DynamicList;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @author Julian Schelker
 */
public class Map {

	public DynamicList<Snake> snakes;

	public Map() {
		this.snakes = new DynamicList<Snake>();
	}

	public Drawable2d getDrawable() {
		return new MapDrawer(this);
	}

}
