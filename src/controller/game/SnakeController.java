package controller.game;

import model.Map;
import model.game.Snake;

public class SnakeController {

	private Map map;

	public SnakeController(Map map) {
		this.map = map;
	}

	public void update() {
		for (Snake s : this.map.snakes) {
			s.move();
		}
	}

}
