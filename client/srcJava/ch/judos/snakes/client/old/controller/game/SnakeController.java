package ch.judos.snakes.client.old.controller.game;

import java.util.ArrayList;

import ch.judos.snakes.client.old.controller.GameI;
import ch.judos.snakes.client.old.model.game.EatablePoint;
import ch.judos.snakes.client.old.model.game.Map;
import ch.judos.snakes.client.old.model.game.Snake;

public class SnakeController {

	private Map map;
	private CollisionController collisionController;
	private GameI game;

	public SnakeController(GameI game) {
		this.map = game.getMap();
		this.game = game;
		this.collisionController = new CollisionController(game);
	}

	public void update() {
		this.collisionController.update();
		for (Snake s : this.map.snakes) {
			s.move();
			this.collisionController.storeSnake(s);
		}
		for (Snake s : this.map.snakes) {
			ArrayList<EatablePoint> points = this.map.eatablePoints.forAllInRange(s.getPoints()
				.get(0).getPoint(), s.getTileRadius() + 5);
			s.changeSize(points.stream().mapToInt(point -> point.getSize()).sum());
			this.map.eatablePoints.removeAll(points);
			if (this.collisionController.doesSnakeDie(s)) {
			}
		}
	}
}
