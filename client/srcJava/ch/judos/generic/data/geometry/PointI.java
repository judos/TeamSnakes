package ch.judos.generic.data.geometry;

import ch.judos.generic.data.RandomJS;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author Julian Schelker
 * @since 09.02.2015
 */
public class PointI extends Point {
	private static final long serialVersionUID = -2304751012108303425L;

	public static PointI randomUniform(int minX, int maxX, int minY, int maxY) {
		return new PointI(RandomJS.getInt(minX, maxX), RandomJS.getInt(minY, maxY));
	}

	public PointI(Point point) {
		this(point.x, point.y);
	}

	public PointI(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public PointI() {
	}

	public double getAngleTo(Point2D target) {
		return Math.atan2(target.getY() - this.y, target.getX() - this.x);
	}

	public Angle getAAngleTo(Point2D target) {
		return Angle.fromRadian(Math.atan2(target.getY() - this.y, target.getX() - this.x));
	}

	public PointI deepCopy() {
		return new PointI(this.x, this.y);
	}

	public boolean inRectFromZero(int width, int height) {
		return this.x >= 0 && this.x < width && this.y >= 0 && this.y < height;
	}

	public PointF f() {
		return new PointF(this);
	}

	public PointI add(PointF scale) {
		return new PointI(this.x + scale.getXI(), this.y + scale.getYI());
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.x + "/" + this.y;
	}

	public PointI subtract(PointI point) {
		return new PointI(this.x - point.x, this.y - point.y);
	}

	public PointI add(PointI point) {
		return new PointI(this.x + point.x, this.y + point.y);
	}

}
