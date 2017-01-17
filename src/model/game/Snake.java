package model.game;

import java.awt.Color;
import java.util.ArrayList;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;

public class Snake {

	private Angle headAngle;
	private ArrayList<PointF> points;
	private int bonusTick;
	private double lastAngle;
	private int index;

	public static final float maxSpeed = 3;
	public static final float spaceBetweenParts = 10;
	private static int currentIndex = 0;

	public Snake(PointF point, Angle angle, int size) {
		this.points = new DynamicList<PointF>(point);
		this.index = currentIndex;
		currentIndex++;
		for (int i = 0; i < size; i++) {
			this.points.add(this.points.get(i).movePoint(angle.turnClockwise(Angle.A_180),
				spaceBetweenParts));
		}
		this.headAngle = angle;
	}

	public Color getColor() {
		return new Color(1 / 1.2f, 0, 0);
	}

	public Angle getHeadAngle() {
		return this.headAngle.clone();
	}

	public ArrayList<PointF> getPoints() {
		return this.points;
	}

	public void move() {
		for (int i = this.points.size() - 1; i > 1; i--) {
			float d = this.points.get(i).distanceTo(this.points.get(i - 1));
			float speed = (d - spaceBetweenParts) / 3 + getMaxSpeed();
			this.points.get(i).approachPoint(this.points.get(i - 1), speed);
		}
		float d = this.points.get(1).distanceTo(this.points.get(0));
		this.points.get(1).approachPoint(this.points.get(0), d - spaceBetweenParts);
		this.points.get(0).movePointI(this.headAngle, getMaxSpeed());
	}

	private float getMaxSpeed() {
		return (float) (maxSpeed * (1 + 0.04 * this.bonusTick));
	}

	private double getMaxTurningSpeed() {
		return 3;
	}

	public void turnIntoDirection(Angle targetAngle) {
		double delta = this.headAngle.approachAngle(targetAngle, Angle
			.fromDegree(getMaxTurningSpeed()));

		if (delta == this.lastAngle && delta != 0) {
			if (this.bonusTick < 10)
				this.bonusTick++;
		}
		else if (this.bonusTick > 0) {
			this.bonusTick--;
		}
		this.lastAngle = delta;
	}

	public int getTileRadius() {
		return 20;
	}

	public static int getMaxTileRadius() {
		return 60;
	}

	@Override
	public String toString() {
		return "Snake " + this.index;
	}
}
