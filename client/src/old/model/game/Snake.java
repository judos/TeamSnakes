package old.model.game;

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
	private int size;

	public float speedupModifier = 1;
	private SnakeTeam team;

	public static final float maxSpeed = 3;
	public static final float spaceBetweenParts = 10;
	private static int currentIndex = 0;

	public Snake(PointF point, Angle angle, int size, SnakeTeam team) {
		this.team = team;
		this.points = new DynamicList<PointF>(point);
		this.index = currentIndex;
		currentIndex++;
		this.size = size;
		for (int i = 0; i < getAmountOfTiles(); i++) {
			this.points.add(this.points.get(i).movePoint(angle.turnClockwise(Angle.A_180),
				spaceBetweenParts));
		}
		this.headAngle = angle;
	}

	public int getAmountOfTiles() {
		return size / 2;
	}

	public int getSize() {
		return this.size;
	}

	public void changeSize(int addition) {
		this.size += addition;
	}

	public Color getColor() {
		return this.team.getColor();
	}

	public Angle getHeadAngle() {
		return this.headAngle.clone();
	}

	public ArrayList<PointF> getPoints() {
		return this.points;
	}

	public void move() {
		PointF lastPos = this.points.get(this.points.size() - 1);
		for (int i = this.points.size() - 1; i > 1; i--) {
			float d = this.points.get(i).distanceTo(this.points.get(i - 1));
			float speed = (d - spaceBetweenParts) / 3 + getMaxSpeed();
			this.points.get(i).approachPoint(this.points.get(i - 1), speed);
		}
		if (this.size / 2 > this.points.size()) {
			this.points.add(lastPos.clone());
		}
		else if (this.size / 2 < this.points.size()) {
			this.points.remove(this.points.size() - 1);
		}
		float d = this.points.get(1).distanceTo(this.points.get(0));
		this.points.get(1).approachPoint(this.points.get(0), d - spaceBetweenParts);
		this.points.get(0).movePointI(this.headAngle, getMaxSpeed());

	}

	private float getMaxSpeed() {
		return (float) (maxSpeed * (1 + 0.04 * this.bonusTick) * speedupModifier);
	}

	private double getMaxTurningSpeed() {
		double turn = 8 - (float) this.size / 2500 * 6;
		if (turn < 2)
			turn = 2;
		return turn;
	}

	public void turnIntoDirection(Angle targetAngle) {
		double delta = this.headAngle.approachAngle(targetAngle, Angle.fromDegree(
			getMaxTurningSpeed()));

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
		return (int) ((float) this.size / 2500 * 40 + 10);
	}

	public static int getMaxTileRadius() {
		return 60;
	}

	@Override
	public String toString() {
		return "Snake " + this.index;
	}

	public SnakeTeam getTeam() {
		return this.team;
	}

}
