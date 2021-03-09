package ch.judos.snakes.client.core.ui;

import ch.judos.snakes.client.core.base.Design;
import ch.judos.snakes.client.core.io.InputEvent;

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

	public void render(Graphics2D g, Point mousePos) {
		this.renderTextWithColor(g, Design.textColor);
	}

	protected void renderTextWithColor(Graphics g, Color c) {
		g.setFont(this.font);
		g.setColor(c);
		g.drawString(this.text, pos.x + Design.textMarginX,
				pos.y + Design.TextMarginY + this.size.height / 2);
	}

	@Override
	public Dimension getPreferedDimension() {
		Dimension textSize = measureTextSize(this.font, getTextForSizeCalculation());
		return new Dimension(textSize.width + 2 * Design.textMarginX,
				textSize.height + 2 * Design.TextMarginY);
	}

	protected String getTextForSizeCalculation() {
		return this.text;
	}

	@Override
	public void handleInput(InputEvent event) {
	}

}
