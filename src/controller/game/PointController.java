package controller.game;

import ch.judos.generic.data.geometry.PointI;
import controller.GameI;
import model.game.DrawingLayer;
import model.game.EatablePoint;
import model.game.Map;
import view.game.EatablePointDrawer;

public class PointController {

	private int amountOfPoints;
	private int maxAmountOfPoints;
	private Map map;

	public PointController(GameI game) {
		this.amountOfPoints = 0;
		this.maxAmountOfPoints = 1000;
		this.map = game.getMap();
		game.getMapDrawer().drawables.put(DrawingLayer.EatablePoints, new EatablePointDrawer(
			game));
	}

	public void update() {
		if (this.amountOfPoints < this.maxAmountOfPoints) {
			EatablePoint p = new EatablePoint(PointI.randomUniform(0, 400, 0, 200), 2);
			this.map.eatablePoints.put(p);
			this.amountOfPoints++;
		}
	}

}
