package model;

import model.game.Snake;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;

/**
 * @author Julian Schelker
 */
public class MapGenerator {

	public static Map getMap() {
		Map m = new Map();
		m.snakes.add(new Snake(new PointF(0, 0), Angle.fromDegree(0), 20));
		return m;
	}

}
