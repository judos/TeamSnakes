package core.window;

import core.base.Window;

import java.awt.*;

public interface GameWindow extends Window {

	void discard();

	Graphics2D getGraphics();

	void flipFrame();

	/**
	 * indicates if the window has been resized since the last call to this method. By default this always returns false
	 * (i.e. resize not supported)
	 */
	default boolean resized() {
		return false;
	}

	public static final Color BG_COLOR = new Color(238, 238, 238);

}
