package controller.game;

import controller.GameI;
import model.game.Map;
import model.game.Snake;

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
			if (this.collisionController.doesSnakeDie(s)) {
			}
		}
	}
}
