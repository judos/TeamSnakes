package model.game;

import ch.judos.generic.data.DynamicList;
import model.game.space.LocationHashMap;

/**
 * @author Julian Schelker
 */
public class Map {

	public DynamicList<Snake> snakes;
	public LocationHashMap<SnakeTile> snakeCollisionMap;
	public LocationHashMap<EatablePoint> eatablePoints;

	public Map() {
		this.snakes = new DynamicList<Snake>();
		this.snakeCollisionMap = new LocationHashMap<>();
		this.eatablePoints = new LocationHashMap<>();
	}

}
