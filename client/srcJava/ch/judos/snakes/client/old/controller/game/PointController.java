package ch.judos.snakes.client.old.controller.game;

import ch.judos.generic.data.geometry.PointI;
import ch.judos.snakes.client.old.controller.GameI;
import ch.judos.snakes.client.old.model.game.DrawingLayer;
import ch.judos.snakes.client.old.model.game.EatablePoint;
import ch.judos.snakes.client.old.model.game.Map;
import ch.judos.snakes.client.old.view.game.EatablePointDrawer;

public class PointController {

	private int maxAmountOfPoints;
	private Map map;

	public PointController(GameI game) {
		this.maxAmountOfPoints = 1000;
		this.map = game.getMap();
		game.getMapDrawer().drawables.put(DrawingLayer.EatablePoints, new EatablePointDrawer(
			game));
	}

	public void update() {
		if (this.map.eatablePoints.getSize() < this.maxAmountOfPoints) {
			EatablePoint p = new EatablePoint(PointI.randomUniform(0, 2000, 0, 1000), 1);
			this.map.eatablePoints.put(p);
		}
	}

}
