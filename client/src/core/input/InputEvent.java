package core.input;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Set;


/**
 * describes any kind of input that can be done by the user
 */
public class InputEvent {

	public static enum InputEventType {
		PRESS,
		RELEASE,
		CHANGE;
	}

	public boolean isConsumed;

	private int keyCode;
	private InputEventType type;
	private Set<Integer> holdKeyCodes;
	private double changeValue;
	private Point currentMousePosition;
	private Character text; // object instead of primitive because it can be null

	public InputEvent(int keyCode, InputEventType type, double changeValue, Set<Integer> holdKeyCodes,
			Point currentMousePos) {
		this(keyCode, type, changeValue, holdKeyCodes, currentMousePos, null);
	}

	/**
	 * @param keyCode
	 * @param type
	 * @param changeValue
	 * @param holdKeyCodes general other keyCodes currently hold down
	 * @param currentMousePos
	 * @param c
	 */
	public InputEvent(int keyCode, InputEventType type, double changeValue, Set<Integer> holdKeyCodes,
			Point currentMousePos, Character c) {
		this.keyCode = keyCode;
		this.text = c;
		this.type = type;
		this.changeValue = changeValue;
		this.holdKeyCodes = holdKeyCodes;
		this.currentMousePosition = currentMousePos;
		this.isConsumed = false;
	}

	public Point getCurrentMousePosition() {
		return currentMousePosition;
	}

	public boolean isPressActionConsumeAndRun(InputAction action, Runnable run) {
		if (isPressActionAndConsume(action)) {
			run.run();
			return true;
		}
		return false;
	}

	public boolean isPressActionAndConsume(InputAction action) {
		if (isPressAction(action)) {
			consume();
			return true;
		}
		return false;
	}

	public boolean isActionAndConsume(InputAction action) {
		if (isAction(action)) {
			consume();
			return true;
		}
		return false;
	}

	public boolean isPressAction(InputAction action) {
		return isPress() && isAction(action);
	}

	public boolean isReleaseAction(InputAction action) {
		return isReleased() && isAction(action);
	}

	public boolean isAction(InputAction action) {
		return action.isActivatedBy(this.keyCode);
	}

	public boolean isPress() {
		return this.type == InputEventType.PRESS;
	}

	public boolean isReleased() {
		return this.type == InputEventType.RELEASE;
	}

	public boolean isChanged() {
		return this.type == InputEventType.CHANGE;
	}

	/**
	 * @return if present any change value (such as mouse scrolling), otherwise 0
	 */
	public double getChangeValue() {
		return this.changeValue;
	}

	public String getText() {
		if (this.text == null)
			return "";
		return String.valueOf(this.text);
	}

	/**
	 * is any other action input hold down while this action was generated
	 * 
	 * @param action
	 * @return
	 */
	public boolean isActionHold(InputAction action) {
		for (int possibleKeyCode : action.getKeyCodes()) {
			if (this.holdKeyCodes.contains(possibleKeyCode))
				return true;
		}
		return false;
	}

	public void consume() {
		this.isConsumed = true;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(
				"InputEvent [" + type + ", " + KeyEvent.getKeyText(this.keyCode) + ", holdKeyCodes=" + holdKeyCodes
						+ ", mousePos=" + currentMousePosition.x+"/"+currentMousePosition.y + ", text=" + text + ", keycode=" + this.keyCode);
		if (this.changeValue != 0)
			buf.append(", changeValue=" + changeValue);
		buf.append("]");
		return buf.toString();
	}

}
