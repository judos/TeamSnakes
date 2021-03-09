package ch.judos.snakes.client.core.ui;

import ch.judos.snakes.client.core.io.InputEvent;

import java.awt.*;

public class Spacer extends BaseComponent {

	private int preferedWidth;
	private int preferedHeight;

	public Spacer(int width, int height) {
		this.preferedWidth = width;
		this.preferedHeight = height;
	}

	@Override
	public Dimension getPreferedDimension() {
		return new Dimension(preferedWidth, preferedHeight);
	}

	@Override
	public void handleInput(InputEvent event) {
	}

	@Override
	public void render(Graphics2D graphics, Point mousePos) {
	}

}
