package ch.judos.snakes.client.old.model.game;

import java.awt.Color;

import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;

/**
 * @author Julian Schelker
 */
public class MapGenerator {

	public static Map getMap() {
		Map m = new Map();

		SnakeTeam team1 = new SnakeTeam(new Color(0, 255, 0));
		m.snakes.add(new Snake(new PointF(0, 0), Angle.fromDegree(0), 20, team1));
		m.snakes.add(new Snake(new PointF(0, 200), Angle.fromDegree(0), 20, team1));
		SnakeTeam team2 = new SnakeTeam(new Color(100, 100, 255));
		m.snakes.add(new Snake(new PointF(0, 100), Angle.fromDegree(0), 20, team2));
		return m;
	}

}
