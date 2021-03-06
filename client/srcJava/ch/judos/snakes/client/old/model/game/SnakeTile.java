package ch.judos.snakes.client.old.model.game;

import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.snakes.client.old.model.game.space.Locatable;

public class SnakeTile implements Locatable {

	private Snake snake;
	private PointF point;

	public SnakeTile(PointF point, Snake snake) {
		this.point = point;
		this.snake = snake;
	}

	public boolean blocksSnake(Snake snake) {
		if (snake == this.snake) // don't block yourself
			return false;
		if (snake.getTeam() == this.snake.getTeam())
			return false;
		PointF head = snake.getPoints().get(0);
		double dist = head.distance(point);
		return dist < this.snake.getTileRadius() + snake.getTileRadius();
	}

	@Override
	public PointI getLocation() {
		return this.point.getPoint();
	}
}
