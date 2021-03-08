package ch.judos.snakes.client.core.ui;

import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionH;
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionV;

public class WindowData {

	public PositionH posH;
	public PositionV posV;

	public WindowData(PositionH posH, PositionV posV) {
		this.posH = posH;
		this.posV = posV;
	}
}
