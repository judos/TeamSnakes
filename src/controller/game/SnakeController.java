package controller.game;

import model.Map;
import model.game.Snake;

public class SnakeController {

	private Map map;
	private CollisionController collisionController;

	public SnakeController(Map map) {
		this.map = map;
		this.collisionController = new CollisionController();
	}

	public void update() {
		this.collisionController.update();
		for (Snake s : this.map.snakes) {
			s.move();
			this.collisionController.storeSnake(s);
		}
		for (Snake s : this.map.snakes) {
			if (this.collisionController.doesSnakeDie(s)) {
				System.out.println("Collision++++++!!!!!");
			}
		}
	}
}
