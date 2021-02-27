package core.ui;

import core.base.Design;
import core.input.InputEvent;

import java.awt.*;


public class Label extends BaseComponent {

	protected String title;
	protected Font font;

	public Label(String title) {
		this(title, false);
	}

	public Label(String title, boolean isTitle) {
		super();
		this.title = title;
		this.font = isTitle ? Design.titleFont : Design.textFont;
	}

	public void render(Graphics g) {
		g.setFont(this.font);
		g.setColor(Design.textColor);
		g.drawString(this.title, pos.x + Design.buttonTextMarginX,
				pos.y + Design.buttonTextMarginY + this.size.height / 2);
	}

	@Override
	public Dimension getPreferedDimension() {
		Dimension textSize = measureTextSize(this.font, getTextForSizeCalculation());
		return new Dimension(textSize.width + 2 * Design.buttonTextMarginX,
				textSize.height + 2 * Design.buttonTextMarginY);
	}

	protected String getTextForSizeCalculation() {
		return this.title;
	}

	@Override
	public void handleInput(InputEvent event) {
	}

}
