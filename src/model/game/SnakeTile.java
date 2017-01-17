package model.game;

import ch.judos.generic.data.geometry.PointF;

public class SnakeTile {

	private Snake snake;
	private PointF point;

	public SnakeTile(PointF point, Snake snake) {
		this.point = point;
		this.snake = snake;
	}

	public boolean blocksSnake(Snake snake) {
		if (snake == this.snake) // don't block yourself
			return false;
		PointF head = snake.getPoints().get(0);
		double dist = head.distance(point);
		return dist < this.snake.getTileRadius() + snake.getTileRadius();
	}
}
