package ch.judos.generic.data.geometry;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Stores a floating point position
 *
 * @author Julian Schelker
 * @version 1.0 / 27.02.2013
 * @since 27.02.2013
 */
public class PointF extends Point2D.Float {

	private static final long serialVersionUID = 1495729872930076211L;

	public void rotate(double angleRadian) {
		double rx = (this.x * Math.cos(angleRadian)) - (this.y * Math.sin(angleRadian));
		double ry = (this.x * Math.sin(angleRadian)) + (this.y * Math.cos(angleRadian));
		this.x = (float) rx;
		this.y = (float) ry;
	}

	public void rotate(Angle angle) {
		rotate(angle.getRadian());
	}

	public PointF(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public PointF(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	public PointF(Point p) {
		this(p.x, p.y);
	}

	public PointF(PointF p) {
		this(p.x, p.y);
	}

	/**
	 * <b>Note:</b> does not change the current instance
	 *
	 * @param p some other point
	 * @return the new point, adding up the position of this point and the given
	 * one
	 */
	public PointF add(PointF p) {
		return new PointF(this.x + p.x, this.y + p.y);
	}

	/**
	 * <b>Note:</b> changes the current instance
	 *
	 * @param p some other point
	 */
	public void addI(PointF p) {
		this.x += p.x;
		this.y += p.y;
	}

	/**
	 * <b>Note:</b> changes the current instance
	 *
	 * @param p some other point
	 */
	public void addI(Point p) {
		this.x += p.x;
		this.y += p.y;
	}

	/**
	 * @param target
	 * @param speed  distance to move the point in the direction of the target
	 * @return whether target is reached or not
	 */
	public boolean approachPoint(Point target, float speed) {
		return approachPoint(new PointF(target), speed);
	}

	/**
	 * @param target
	 * @param speed  distance to move the point in the direction of the target
	 * @return whether target is reached or not
	 */
	public boolean approachPoint(PointF target, float speed) {
		PointF delta = target.subtract(this);
		double dist = delta.hypot();
		if (dist <= speed) {
			this.x = target.x;
			this.y = target.y;
			return true;
		}
		double angle = Math.atan2(delta.y, delta.x);
		movePointI(angle, speed);
		return false;
	}

	/**
	 * get a copy of this point<br>
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public PointF clone() {
		return new PointF(this.x, this.y);
	}

	/**
	 * @param pos
	 * @return the distance between this and the given point
	 */
	public float distanceTo(PointF pos) {
		return (float) Math.hypot(this.x - pos.x, this.y - pos.y);
	}

	/**
	 * @param pos
	 * @return the distance between this and the given point
	 */
	public float distanceTo(Point pos) {
		return (float) Math.hypot(this.x - pos.x, this.y - pos.y);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PointF))
			return false;
		PointF p = (PointF) obj;
		return p.x == this.x && p.y == this.y;
	}

	public double getAngleTo(Point2D target) {
		return Math.atan2(target.getY() - this.y, target.getX() - this.x);
	}

	public Angle getAAngleTo(Point2D target) {
		return Angle.fromRadian(Math.atan2(target.getY() - this.y, target.getX() - this.x));
	}

	/**
	 * @return a common awt int-based point
	 */
	public PointI getPoint() {
		return new PointI(getXI(), getYI());
	}

	public PointI i() {
		return getPoint();
	}

	/**
	 * @return X as integer
	 */
	public int getXI() {
		return Math.round(this.x);
	}

	/**
	 * @return Y as integer
	 */
	public int getYI() {
		return Math.round(this.y);
	}

	/**
	 * @return calculates the hypthenus of this point itself, representing the
	 * distance to the coordinates' origin
	 */
	public double hypot() {
		return Math.hypot(this.x, this.y);
	}

	public void movePointI(double angle, float step) {
		this.x += (float) (step * Math.cos(angle));
		this.y += (float) (step * Math.sin(angle));
	}

	public void movePointI(Angle angle, double step) {
		this.x += (float) (step * angle.getCos());
		this.y += (float) (step * angle.getSin());
	}

	public PointF movePoint(double angle, float step) {
		return new PointF(this.x + (float) (step * Math.cos(angle)), this.y + (float) (step
				* Math.sin(angle)));
	}

	public PointF movePoint(Angle angle, double step) {
		return this.add(angle.getDirection().scale(step));
	}

	/**
	 * @param factor
	 * @return the length of the vector is multiplied by the given factor and
	 * the new vector is returned
	 */
	public PointF scale(double factor) {
		return new PointF((float) (this.x * factor), (float) (this.y * factor));
	}

	/**
	 * @param factor
	 * @return the length of the vector is multiplied by the given factor and
	 * the new vector is returned
	 */
	public PointF scale(float factor) {
		return new PointF(this.x * factor, this.y * factor);
	}

	public void scaleI(double factor) {
		this.x *= factor;
		this.y *= factor;
	}

	/**
	 * @param p
	 * @return the new point, where the position is this' point position minus
	 * the given one
	 */
	public PointF subtract(PointF p) {
		return new PointF(this.x - p.x, this.y - p.y);
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

	/**
	 * @return the length of the vector represented by this point
	 */
	public double length() {
		return hypot();
	}

	/**
	 * modifies the vector represented by this point such that it has the length
	 * of 1.
	 */
	public void normalize() {
		this.scaleI(1 / this.length());
	}

	/**
	 * sets the value of this point
	 *
	 * @param x
	 * @param y
	 */
	public void setValue(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * sets the length of the vector represented by this point
	 *
	 * @param forceMaxLength
	 */
	public void scaleToI(double forceMaxLength) {
		this.scaleI(forceMaxLength / this.length());
	}

	/**
	 * @param p
	 * @return the new point, where the position is this' point position minus
	 * the given one
	 */
	public PointF subtract(Point p) {
		return new PointF(this.x - p.x, this.y - p.y);
	}

	/**
	 * <b>Note:</b> does not change the current instance
	 *
	 * @param p some other point
	 * @return the new point, adding up the position of this point and the given
	 * one
	 */
	public PointF add(Point p) {
		return new PointF(this.x + p.x, this.y + p.y);
	}

	public PointI getPointRounded() {
		return new PointI(Math.round(this.x), Math.round(this.y));
	}

}
