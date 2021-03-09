package ch.judos.snakes.client.core.ui;

import ch.judos.snakes.client.core.base.Design;
import ch.judos.snakes.client.core.io.InputAction;
import ch.judos.snakes.client.core.io.InputEvent;

import java.awt.*;
import java.util.Objects;

public class Button extends Label {

	public enum Colored {
		DEFAULT, CAUTION
	}

	public final Runnable action;
	protected boolean isEnabled;
	protected InputAction hotkeyAction = null;
	protected Color bg;
	protected Color hover;

	public Button(String title, Runnable action) {
		super(title);
		this.action = action;
		this.isEnabled = true;
		setColored(Colored.DEFAULT);
	}

	public Button setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		return this;
	}

	public Button setColored(Colored colored) {
		if (colored == Colored.CAUTION) {
			bg = Design.buttonBackgroundCaution;
			hover = Design.buttonHoverCaution;
		} else {
			bg = Design.buttonBackground;
			hover = Design.buttonHover;
		}
		return this;
	}

	public void render(Graphics2D g, Point mousePos) {
		g.setColor(this.bg);
		if (this.isEnabled && isPointInside(mousePos)) {
			g.setColor(this.hover);
		}
		if (!this.isEnabled) g.setColor(Design.buttonDisabled);
		g.fillRect(pos.x, pos.y, size.width, size.height);
		g.setColor(Design.buttonBorder);
		g.drawRect(pos.x, pos.y, size.width - 1, size.height - 1);
		this.renderTextWithColor(g, this.isEnabled ? Design.textColor : Design.textColorDisabled);
	}

	public Button setInputAction(InputAction hotkeyAction) {
		this.hotkeyAction = hotkeyAction;
		return this;
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressAction(InputAction.SELECT) && isPointInside(event.getCurrentMousePosition()) && this.isEnabled) {
			event.consume();
			this.action.run();
			return;
		}
		if (this.hotkeyAction != null) {
			event.isPressActionConsumeAndRun(this.hotkeyAction, this.action);
		}
	}

	@Override
	public String toString() {
		return "Button(" + this.text + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Button button = (Button) o;
		return isEnabled == button.isEnabled && Objects.equals(action, button.action) &&
				hotkeyAction == button.hotkeyAction &&
				Objects.equals(bg, button.bg) && Objects.equals(hover, button.hover);
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, isEnabled, hotkeyAction, bg, hover);
	}
}
