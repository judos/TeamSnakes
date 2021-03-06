package ch.judos.snakes.client.old.model.input;

import java.awt.event.MouseWheelEvent;

/**
 * @author Julian Schelker
 */
public class MouseWheelEvent2 {

	protected int scrollAmount;
	protected double rotation;

	public MouseWheelEvent2(MouseWheelEvent e) {
		this.scrollAmount = e.getScrollAmount();
		this.rotation = e.getPreciseWheelRotation();
	}

	public int getScrollAmount() {
		return this.scrollAmount;
	}

	public double getRotation() {
		return this.rotation;
	}

}
