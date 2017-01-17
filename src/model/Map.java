package model;

import model.game.Snake;
import view.MapDrawer;
import ch.judos.generic.data.DynamicList;

/**
 * @author Julian Schelker
 */
public class Map {

	public DynamicList<Snake> snakes;
	private MapDrawer drawer;

	public Map() {
		this.snakes = new DynamicList<Snake>();
		this.drawer = new MapDrawer(this);
	}

	public MapDrawer getMapDrawer() {
		return this.drawer;
	}

}
