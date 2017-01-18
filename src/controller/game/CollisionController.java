package controller.game;

import java.util.ArrayList;

import ch.judos.generic.data.HashMapList;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import controller.GameI;
import model.game.DrawingLayer;
import model.game.Snake;
import model.game.SnakeTile;
import model.game.helper.GridHashing;
import view.game.BackgroundDrawer;

public class CollisionController {
	public static final int gridSize = 150;

	private HashMapList<Integer, SnakeTile> snakeCollisionMap;
	private GameI game;

	public CollisionController(GameI game) {
		this.game = game;
		this.snakeCollisionMap = new HashMapList<Integer, SnakeTile>();
		game.getMapDrawer().drawables.put(DrawingLayer.Background, new BackgroundDrawer(game,
			this.snakeCollisionMap));
	}

	public void update() {
		this.snakeCollisionMap.clear();
	}

	public void storeSnake(Snake s) {
		s.getPoints().forEach(point -> {
			SnakeTile tile = new SnakeTile(point, s);
			int index = GridHashing.hashPointIntoMap(GridHashing.hashPoint(point));
			this.snakeCollisionMap.put(index, tile);
		});
	}

	public boolean doesSnakeDie(Snake snake) {
		PointF head = snake.getPoints().get(0);
		double maxDistance = Snake.getMaxTileRadius();
		PointF min = head.subtract(new PointF(maxDistance, maxDistance));
		PointF max = head.add(new PointF(maxDistance, maxDistance));
		PointI minGridIndex = GridHashing.hashPoint(min);
		PointI maxGridIndex = GridHashing.hashPoint(max);
		for (int x = minGridIndex.x; x <= maxGridIndex.x; x++) {
			for (int y = minGridIndex.y; y <= maxGridIndex.y; y++) {
				int index = GridHashing.hashPointIntoMap(new PointI(x, y));
				ArrayList<SnakeTile> list = this.snakeCollisionMap.getList(index);
				if (list != null) {
					for (SnakeTile tile : list) {
						if (tile.blocksSnake(snake)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
