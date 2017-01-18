package model.game;

import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;

/**
 * @author Julian Schelker
 */
public class MapGenerator {

	public static Map getMap() {
		Map m = new Map();
		m.snakes.add(new Snake(new PointF(1000, 1000), Angle.fromDegree(0), 200));
		m.snakes.add(new Snake(new PointF(0, 100), Angle.fromDegree(0), 200));
		return m;
	}

}
