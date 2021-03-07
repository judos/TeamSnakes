package ch.judos.snakes.client.core.ui;

import ch.judos.snakes.client.core.io.InputEvent;
import ch.judos.snakes.client.core.base.Design;

import java.awt.*;


public class Label extends BaseComponent {

	public String text;
	protected Font font;

	public Label(String text) {
		this(text, false);
	}

	public Label(String text, boolean isTitle) {
		super();
		this.text = text;
		this.font = isTitle ? Design.titleFont : Design.textFont;
	}

	public void render(Graphics g) {
		g.setFont(this.font);
		g.setColor(Design.textColor);
		g.drawString(this.text, pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	@Override
	public Dimension getPreferedDimension() {
		Dimension textSize = measureTextSize(this.font, getTextForSizeCalculation());
		return new Dimension(textSize.width + 2 * Design.buttonTextMarginX,
				textSize.height + 2 * Design.buttonTextMarginY);
	}

	protected String getTextForSizeCalculation() {
		return this.text;
	}

	@Override
	public void handleInput(InputEvent event) {
	}

}
