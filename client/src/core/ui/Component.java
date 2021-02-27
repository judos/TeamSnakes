package core.ui;

import core.base.BaseRenderer;
import core.input.InputHandler;

import java.awt.*;

/**
 * a component is supposed to initialize its base size as the prefered size
 */
public interface Component extends InputHandler, BaseRenderer {

	/**
	 * called by the super component before drawing happens to decide on the
	 * absolute position of the component
	 *
	 * @param x the maximum available space for this component
	 */
	public void layout(int x, int y, int w, int h);

	public Dimension getPreferedDimension();

	public int getStretchWeightX();

	public int getStretchWeightY();

}
