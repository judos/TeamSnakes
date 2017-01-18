package model.game;

import ch.judos.generic.data.DynamicList;

/**
 * @author Julian Schelker
 */
public class Map {

	public DynamicList<Snake> snakes;

	public Map() {
		this.snakes = new DynamicList<Snake>();
	}

}
