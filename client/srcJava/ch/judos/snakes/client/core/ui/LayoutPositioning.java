package ch.judos.snakes.client.core.ui;

import java.awt.*;

public class LayoutPositioning {

	public enum PositionH {
		LEFT,
		CENTER,
		RIGHT;
	}

	public enum PositionV {
		TOP,
		CENTER,
		BOTTOM;
	}

	private PositionH posH;
	private PositionV posV;
	private Dimension size;
	private boolean stretchX = false;
	private boolean stretchY = false;

	public LayoutPositioning(PositionH posH, PositionV posV, Dimension size) {
		this.posH = posH;
		this.posV = posV;
		this.size = size;
	}

	public LayoutPositioning setStretch(boolean stretchX, boolean stretchY) {
		this.stretchX = stretchX;
		this.stretchY = stretchY;
		return this;
	}

	public Point getPixelPositionInBounds(int x, int y, int w, int h) {
		int px, py;
		if (this.posH == PositionH.LEFT)
			px = x;
		else if (this.posH == PositionH.CENTER)
			px = x + w / 2 - this.size.width / 2;
		else
			px = x + w - this.size.width;
		if (this.posV == PositionV.TOP)
			py = y;
		else if (this.posV == PositionV.CENTER)
			py = y + h / 2 - this.size.height / 2;
		else
			py = y + h - this.size.height;
		return new Point(px, py);
	}

	public int[] getInBounds(int x, int y, int w, int h) {
		int[] r = new int[4];
		if (this.stretchX || this.posH == PositionH.LEFT) {
			r[0] = x;
			r[2] = stretchX ? w : size.width;
		} else if (this.posH == PositionH.CENTER) {
			r[0] = x + w / 2 - this.size.width / 2;
			r[2] = size.width;
		} else {
			r[0] = x + w - this.size.width;
			r[2] = size.width;
		}
		if (this.stretchY || this.posV == PositionV.TOP) {
			r[1] = y;
			r[3] = stretchY ? h : size.height;
		} else if (this.posV == PositionV.CENTER) {
			r[1] = y + h / 2 - this.size.height / 2;
			r[3] = size.height;
		} else {
			r[1] = y + h - this.size.height;
			r[3] = size.height;
		}
		return r;
	}

}
