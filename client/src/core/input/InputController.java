package core.input;

import core.base.NamedComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static core.input.InputEvent.InputEventType.*;

public class InputController implements NamedComponent {

	protected final Logger logger = LogManager.getLogger(getClass());

	// CONSTANTS
	public static final int MOUSE_LEFT = 0x0001_0000;
	public static final int MOUSE_CENTER = 0x0002_0000;
	public static final int MOUSE_RIGHT = 0x0003_0000;
	public static final int MOUSE_SCROLL = 0x0004_0000;

	// EVENT PROCESSING
	private List<InputEvent> queuedInputEvents;

	// CACHE
	private HashSet<Integer> holdKeyCodes;
	private Point currentMousePosition;
	private InputHandler currentlyFocused;

	public InputController() {
		queuedInputEvents = new ArrayList<>();
		this.holdKeyCodes = new HashSet<>();
		this.currentMousePosition = new Point(-1, -1);
		this.hoveringEnabled = true;
	}

	public List<InputEvent> popAllEvents() {
		List<InputEvent> result = queuedInputEvents;
		queuedInputEvents = new ArrayList<>();
		return result;
	}

	public InputHandler getCurrentlyFocused() {
		return currentlyFocused;
	}

	// ADAPTERS
	private KeyAdapter keyboard = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if (keycode == 0) keycode = e.getExtendedKeyCode();
			Character c = e.getKeyChar();
			if (!Character.isDefined(c)) c = null;
			queuedInputEvents.add(new InputEvent(keycode, PRESS, 0, new HashSet<>(holdKeyCodes), currentMousePosition, c));

			holdKeyCodes.add(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int keycode = e.getKeyCode();
			if (keycode == 0) keycode = e.getExtendedKeyCode();
			holdKeyCodes.remove(e.getKeyCode());
			queuedInputEvents.add(new InputEvent(keycode, RELEASE, 0, new HashSet<>(holdKeyCodes), currentMousePosition));
		}
	};

	public KeyAdapter getKeyboard() {
		return keyboard;
	}
	private MouseAdapter mouse = new MouseAdapter() {

		@Override
		public void mouseDragged(MouseEvent e) {
			this.mouseMoved(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point p = e.getPoint();
			if (p == null) return;
			currentMousePosition = p;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int code = btnToCode(e.getButton());
			queuedInputEvents.add(new InputEvent(code, PRESS, 0, holdKeyCodes, currentMousePosition));
			holdKeyCodes.add(code);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int code = btnToCode(e.getButton());
			holdKeyCodes.remove(code);
			queuedInputEvents.add(new InputEvent(code, RELEASE, 0, holdKeyCodes, currentMousePosition));
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int code = MOUSE_SCROLL;
			queuedInputEvents.add(new InputEvent(code, CHANGE, e.getPreciseWheelRotation(), holdKeyCodes, currentMousePosition));
		}

		private int btnToCode(int button) {
			if (button == MouseEvent.BUTTON1) return MOUSE_LEFT;
			if (button == MouseEvent.BUTTON2) return MOUSE_CENTER;
			if (button == MouseEvent.BUTTON3) return MOUSE_RIGHT;
			logger.warn("Unknown mouse button: " + button);
			return MOUSE_CENTER;
		}

	};

	public MouseAdapter getMouse() {
		return mouse;
	}
	private boolean hoveringEnabled;

	/**
	 * @return mouse coordinate, DO NOT MODIFY THIS OBJECT!
	 */
	public Point getMousePosition() {
		if (!this.hoveringEnabled) {
			return new Point(-2, -2);
		}
		return this.currentMousePosition;
	}

	public void setFocus(InputHandler handler) {
		this.currentlyFocused = handler;
	}

	public void unfocus(InputHandler handler) {
		if (this.currentlyFocused == handler) this.currentlyFocused = null;
	}

	public void handleInput(InputEvent event) {
		if (this.currentlyFocused != null) {
			if (event.isPressActionAndConsume(InputAction.DESELECT)) {
				this.currentlyFocused.unfocus();
				this.currentlyFocused = null;
			} else {
				this.currentlyFocused.handleInput(event);
			}
		}
	}

	public void setHoverEnabled(boolean b) {
		this.hoveringEnabled = b;
	}

}
