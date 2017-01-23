package controller.game;

import ch.judos.generic.data.geometry.PointI;
import controller.GameI;
import model.game.DrawingLayer;
import model.game.EatablePoint;
import model.game.Map;
import view.game.EatablePointDrawer;

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
