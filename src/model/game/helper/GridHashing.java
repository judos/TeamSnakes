package model.game.helper;

import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import controller.game.CollisionController;

public class GridHashing {

	public static PointI hashPoint(PointF point) {
		return new PointI(point.getXI() / CollisionController.gridSize, point.getYI()
			/ CollisionController.gridSize);
	}
	public static int hashPointIntoMap(PointI point) {
		return point.x + point.y * 17;
	}
}
