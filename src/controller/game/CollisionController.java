package controller.game;

import java.util.ArrayList;

import model.game.Snake;
import model.game.SnakeTile;
import ch.judos.generic.control.Log;
import ch.judos.generic.data.HashMapList;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;

public class CollisionController {
	public static final int gridSize = 100;

	private HashMapList<Integer, SnakeTile> snakeCollisionMap;

	public CollisionController() {
		this.snakeCollisionMap = new HashMapList<Integer, SnakeTile>();
	}

	public void update() {
		this.snakeCollisionMap.clear();
	}

	public void storeSnake(Snake s) {
		s.getPoints().forEach(point -> {
			SnakeTile tile = new SnakeTile(point, s);
			int index = hashPointIntoMap(hashPoint(point));
			this.snakeCollisionMap.put(index, tile);
		});
	}

	public PointI hashPoint(PointF point) {
		return new PointI(point.getXI() / gridSize, point.getYI() / gridSize);
	}
	public int hashPointIntoMap(PointI point) {
		return point.x + point.y * 17;
	}

	public boolean doesSnakeDie(Snake snake) {
		PointF head = snake.getPoints().get(0);
		int it = 0;
		double maxDistance = Snake.getMaxTileRadius();
		PointF min = head.subtract(new PointF(maxDistance, maxDistance));
		PointF max = head.add(new PointF(maxDistance, maxDistance));
		PointI minGridIndex = hashPoint(min);
		PointI maxGridIndex = hashPoint(max);
		for (int x = minGridIndex.x; x <= maxGridIndex.x; x++) {
			for (int y = minGridIndex.y; y <= maxGridIndex.y; y++) {
				int index = hashPointIntoMap(new PointI(x, y));
				ArrayList<SnakeTile> list = this.snakeCollisionMap.getList(index);
				if (list != null) {
					for (SnakeTile tile : list) {
						it++;
						if (tile.blocksSnake(snake)) {
							Log.info("Iterations: " + it + " snake " + snake);
							return true;
						}
					}
				}
			}
		}
		Log.info("Iterations: " + it + " snake " + snake);
		return false;
	}
}
