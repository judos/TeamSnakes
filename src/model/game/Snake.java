package model.game;

import java.awt.Color;
import java.util.ArrayList;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;

public class Snake {

	public Angle headAngle;
	private ArrayList<PointF> points;

	public static final float speed = 3;
	public static final float spaceBetweenParts = 10;
	public static final float circleSize = 40;

	public Snake(PointF point, Angle angle, int size) {
		this.points = new DynamicList<PointF>(point);
		for (int i = 1; i <= size; i++) {
			this.points.add(this.points.get(i - 1).movePoint(angle.turnClockwise(Angle.A_180),
				spaceBetweenParts));
		}
		this.headAngle = angle;
	}

	public Color getColor() {
		return Color.red;
	}

	public Angle getHeadAngle() {
		return this.headAngle;
	}

	public ArrayList<PointF> getPoints() {
		return this.points;
	}

	public void move() {
		for (int i = this.points.size() - 1; i > 0; i--) {
			this.points.get(i).approachPoint(this.points.get(i - 1), speed);
		}
		this.points.get(0).movePointI(this.headAngle, speed);
	}
}
