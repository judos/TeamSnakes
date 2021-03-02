package old.model.input;

import java.awt.event.KeyEvent;

/**
 * @author Julian Schelker
 */
public class KeyEvent2 {
	private int keyCode;
	private InputType type;

	public KeyEvent2(InputType type, KeyEvent e) {
		this.type = type;
		this.keyCode = e.getKeyCode();
	}

	/**
	 * @return the keyCode
	 */
	public int getKeyCode() {
		return this.keyCode;
	}

	/**
	 * @return the type
	 */
	public InputType getType() {
		return this.type;
	}

}
