package ch.judos.snakes.client.core.ui;

import ch.judos.snakes.client.core.base.Design;
import ch.judos.snakes.client.core.io.InputAction;
import ch.judos.snakes.client.core.io.InputController;
import ch.judos.snakes.client.core.io.InputEvent;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CheckboxButton extends Label {

	public final InputController input;
	private AtomicBoolean dataMapping;

	public CheckboxButton(String title, InputController input, AtomicBoolean mapper) {
		super(title);
		this.input = input;
		this.dataMapping = mapper;
	}

	@Override
	public void render(Graphics2D g, Point mousePos) {
		Stroke stroke = g.getStroke();
		Color color = Design.buttonBackground;
		if (isPointInside(this.input.getMousePosition()))
			color = Design.buttonHover;
		g.setColor(color);
		g.fillRect(pos.x, pos.y, size.width, size.height);

		color = Design.checkboxNotSelected;
		if (this.dataMapping.get())
			color = Design.checkboxSelected;
		g.setColor(color);
		g.fillRect(pos.x + 5, pos.y + size.height / 2 - 10, 20, 20);
		g.setColor(Design.buttonBorder);
		g.drawRect(pos.x, pos.y, size.width - 1, size.height - 1);
		if (this.dataMapping.get()) {
			g.setStroke(new BasicStroke(3));
			g.drawLine(pos.x + 10, pos.y + size.height / 2, pos.x + 15, pos.y + size.height / 2 + 5);
			g.drawLine(pos.x + 15, pos.y + size.height / 2 + 5, pos.x + 20, pos.y + size.height / 2 - 5);
		}
		g.setFont(Design.textFont);
		g.setColor(Design.textColor);
		g.drawString(this.text, 30 + pos.x + Design.textMarginX,
				pos.y + Design.TextMarginY + this.size.height / 2);
		g.setStroke(stroke);
	}

	@Override
	public Dimension getPreferedDimension() {
		Dimension baseSize = super.getPreferedDimension();
		baseSize.width += 30;
		return baseSize;
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressAction(InputAction.SELECT) && isPointInside(event.getCurrentMousePosition())) {
			event.consume();
			this.dataMapping.set(!this.dataMapping.get());
		}
	}

}
