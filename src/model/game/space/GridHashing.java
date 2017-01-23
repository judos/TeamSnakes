package model.game.space;

import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;

public class GridHashing {

	public static final int gridSize = 150;

	public static PointI hashPointToGridIndex(PointF point) {
		return new PointI(fDiv(point.getXI()), fDiv(point.getYI()));
	}
	public static PointI hashPointToGridIndex(PointI point) {
		return new PointI(fDiv(point.x), fDiv(point.y));
	}

	public static int hashGridPointIntoMapIndex(PointI point) {
		return point.x + point.y * 17;
	}

	public static int hashPointIToMapIndex(PointI point) {
		return hashGridPointIntoMapIndex(hashPointToGridIndex(point));
	}

	private static int fDiv(int pos) {
		return Math.floorDiv(pos, gridSize);
	}

}
