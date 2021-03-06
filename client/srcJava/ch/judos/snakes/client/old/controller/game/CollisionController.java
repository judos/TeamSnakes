package ch.judos.snakes.client.old.controller.game;

import ch.judos.generic.data.geometry.PointI;
import ch.judos.snakes.client.old.controller.GameI;
import ch.judos.snakes.client.old.model.game.DrawingLayer;
import ch.judos.snakes.client.old.model.game.Map;
import ch.judos.snakes.client.old.model.game.Snake;
import ch.judos.snakes.client.old.model.game.SnakeTile;
import ch.judos.snakes.client.old.view.game.BackgroundDrawer;

public class CollisionController {
	private GameI game;
	private Map map;

	public CollisionController(GameI game) {
		this.game = game;
		this.map = game.getMap();
		game.getMapDrawer().drawables.put(DrawingLayer.Background, new BackgroundDrawer(game));
	}

	public void update() {
		this.map.snakeCollisionMap.clear();
	}

	public void storeSnake(Snake s) {
		s.getPoints().forEach(point -> {
			SnakeTile tile = new SnakeTile(point, s);
			this.map.snakeCollisionMap.put(tile);
		});
	}

	public boolean doesSnakeDie(Snake snake) {
		PointI head = snake.getPoints().get(0).getPoint();
		int maxDistance = Snake.getMaxTileRadius();

		// Arrays.stream(new int[] {1,2,3,4,5}).map

		this.map.snakeCollisionMap.forEachInRange(head, maxDistance, snakeTile -> {
			if (snakeTile.blocksSnake(snake)) {
			}
			return snakeTile.blocksSnake(snake);
		});
		return false;
	}
}
