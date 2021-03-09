package ch.judos.snakes.client.core.ui;

import ch.judos.generic.data.StringUtils;
import ch.judos.snakes.client.core.base.Design;
import ch.judos.snakes.client.core.io.InputAction;
import ch.judos.snakes.client.core.io.InputController;
import ch.judos.snakes.client.core.io.InputEvent;
import ch.judos.snakes.client.core.io.TextEditing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class InputField extends Label {

	private Logger logger = LogManager.getLogger(getClass());
	private int expectedLength;
	private InputController input;
	private TextEditing text;
	private boolean focused;

	public Runnable enterAction;

	public InputField(int expectedLength, InputController input) {
		super(null);
		this.input = input;
		this.text = new TextEditing("");
		this.expectedLength = expectedLength;
		this.focused = false;
	}

	public InputField setNumbersOnly() {
		this.text.setNumbersOnly();
		return this;
	}

	@Override
	public void unfocus() {
		this.focused = false;
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressAction(InputAction.SELECT)) {
			if (this.isPointInside(event.getCurrentMousePosition())) {
				event.consume();
				this.input.setFocus(this);
				this.focused = true;
			} else {
				this.input.unfocus(this);
				this.focused = false;
			}
		} else if (this.focused) {
			this.text.handleInput(event);
			if (!event.isConsumed && event.isPressActionAndConsume(InputAction.CONFIRM)) {
				if (this.enterAction != null)
					this.enterAction.run();
			}
		}
	}

	@Override
	public void render(Graphics2D g, Point mousePos) {
		g.setColor(Design.textFieldBg);
		if (this.focused)
			g.setColor(Design.textFieldFocus);
		g.fillRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.setColor(Design.textFieldBorder);
		g.drawRect(this.pos.x, this.pos.y, this.size.width - 1, this.size.height - 1);
		if (this.focused) {
			g.setColor(Design.textFieldBorderFocus);
			g.drawRect(this.pos.x + 1, this.pos.y + 1, this.size.width - 2, this.size.height - 2);
		}
		g.setFont(Design.textFont);
		if (!this.focused && this.text.getText().isEmpty()) {
			g.setColor(Design.textColorDisabled);
			g.drawString(".......", pos.x + Design.textMarginX,
					pos.y + Design.TextMarginY + this.size.height / 2);
		} else {
			g.setColor(Design.textColor);
			g.drawString(this.text.getTextWithCursor(this.focused), pos.x + Design.textMarginX,
					pos.y + Design.TextMarginY + this.size.height / 2);
		}
	}

	@Override
	protected String getTextForSizeCalculation() {
		return StringUtils.repeat("W", this.expectedLength);
	}

	public void focus() {
		this.focused = true;
	}

	public String getText() {
		return this.text.getText();
	}

	public int getNumber() {
		return Integer.valueOf(getText());
	}

	public void setText(String text) {
		this.text.setText(text);
	}

	public void setText(int number) {
		this.text.setText("" + number);
	}

}
