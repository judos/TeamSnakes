package ch.judos.snakes.client.old.model.game;

import ch.judos.snakes.client.old.model.game.space.LocationHashMap;

import java.util.ArrayList;

public class Map {

	public static final int gridSize = 150;
	public ArrayList<Snake> snakes;
	public LocationHashMap<SnakeTile> snakeCollisionMap;
	public LocationHashMap<EatablePoint> eatablePoints;

	public Map() {
		this.snakes = new ArrayList<>();
		this.snakeCollisionMap = new LocationHashMap<>(gridSize);
		this.eatablePoints = new LocationHashMap<>(gridSize);
	}

}
