package model.game;

import java.awt.Color;
import java.util.ArrayList;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;

public class Snake {

	public Angle headAngle;
	private ArrayList<PointF> points;

	public static final float maxSpeed = 3;
	public static final float spaceBetweenParts = 15;
	public static final float circleSize = 40;

	public Snake(PointF point, Angle angle, int size) {
		this.points = new DynamicList<PointF>(point);
		for (int i = 0; i < size; i++) {
			this.points.add(this.points.get(i).movePoint(angle.turnClockwise(Angle.A_180),
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
		for (int i = this.points.size() - 1; i > 1; i--) {
			float d = this.points.get(i).distanceTo(this.points.get(i - 1));
			float speed = (d - spaceBetweenParts) / 3 + maxSpeed;
			this.points.get(i).approachPoint(this.points.get(i - 1), speed);
		}
		float d = this.points.get(1).distanceTo(this.points.get(0));
		this.points.get(1).approachPoint(this.points.get(0), d - spaceBetweenParts);
		this.points.get(0).movePointI(this.headAngle, maxSpeed);
	}

	public double getMaxTurningSpeed() {
		return 3;
	}
}
