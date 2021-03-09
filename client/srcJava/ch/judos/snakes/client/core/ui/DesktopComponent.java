package ch.judos.snakes.client.core.ui;

import ch.judos.snakes.client.core.io.InputController;
import ch.judos.snakes.client.core.io.InputEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static ch.judos.snakes.client.core.ui.LayoutPositioning.PositionH;
import static ch.judos.snakes.client.core.ui.LayoutPositioning.PositionV;

/**
 * organize multiple WindowComponents, handling correct draw & input order, also responsible for moving and alwaysOnTop
 * property
 */
public class DesktopComponent extends BaseComponent {

	/**
	 * sorted by z-index. Last window will be on top of everything (getting input first)
	 */
	private final ArrayList<WindowComponent> windows;
	private final HashMap<WindowComponent, WindowData> windowData;
	private final InputController input;

	public DesktopComponent(InputController input) {
		super();
		this.input = input;
		this.windows = new ArrayList<>();
		this.windowData = new HashMap<>();
	}

	public void addWindow(WindowComponent window) {
		addWindow(window, PositionH.CENTER, PositionV.CENTER);
	}

	public void addWindow(WindowComponent window, PositionH posH, PositionV posV) {
		this.windows.add(window);
		this.windowData.put(window, new WindowData(posH, posV));

		Dimension windowSize = window.getPreferedDimension();
		LayoutPositioning layout = new LayoutPositioning(posH, posV, windowSize);
		Point pos = layout.getPixelPositionInBounds(this.pos.x, this.pos.y, this.size.width, this.size.height);

		window.layout(pos.x, pos.y, windowSize.width, windowSize.height);
	}

	public void layoutWindow(WindowComponent window) {
		if (!this.windowData.containsKey(window)) {
			return;
		}
		WindowData data = this.windowData.get(window);
		Dimension windowSize = window.getPreferedDimension();
		LayoutPositioning layout = new LayoutPositioning(data.posH, data.posV, windowSize);
		Point pos = layout.getPixelPositionInBounds(this.pos.x, this.pos.y, this.size.width, this.size.height);
		window.layout(pos.x, pos.y, windowSize.width, windowSize.height);
	}

	@Override
	public void render(Graphics2D g, Point mousePos) {
		int activateHoveringAt = 0;
		this.windows.removeIf(window -> window.isDisposed);

		// traverse windows in reverse to check which windows should be able to show hovering on components
		for (int i = this.windows.size() - 1; i >= 0; i--) {
			WindowComponent window = this.windows.get(i);
			if (window.isPointInside(this.input.getMousePosition())) {
				activateHoveringAt = i;
				break;
			}
		}

		this.windows.sort((win1, win2) -> win1.zIndex - win2.zIndex);
		int zIndex = 0;
		this.input.setHoverEnabled(false);
		for (int i = 0; i < this.windows.size(); i++) {
			if (i == activateHoveringAt)
				this.input.setHoverEnabled(true);
			WindowComponent window = this.windows.get(i);
			window.zIndex = zIndex;
			window.render(g, mousePos);
			zIndex++;
		}
		this.input.setHoverEnabled(true);
	}

	@Override
	public void handleInput(InputEvent event) {
		// last window is on top, thus receiving input first
		for (int i = this.windows.size() - 1; i >= 0; i--) {
			WindowComponent window = this.windows.get(i);
			window.handleInput(event);
			if (event.isConsumed)
				break;
		}
	}

	/**
	 * Desktop Component does not have a preferred size
	 */
	@Override
	public Dimension getPreferedDimension() {
		return new Dimension(0, 0);
	}

}
