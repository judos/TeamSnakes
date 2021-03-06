package ch.judos.snakes.client.core.ui;

import java.awt.Dimension;
import java.awt.Point;

public class LayoutPositioning {

	public static enum PositionH {
		LEFT,
		CENTER,
		RIGHT;
	}

	public static enum PositionV {
		TOP,
		CENTER,
		BOTTOM;
	}

	private PositionH posH;
	private PositionV posV;
	private Dimension size;

	public LayoutPositioning(PositionH posH, PositionV posV, Dimension size) {
		this.posH = posH;
		this.posV = posV;
		this.size = size;
	}

	public Point getPixelPositionBasedOnEnums(int x, int y, int w, int h) {
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

}
