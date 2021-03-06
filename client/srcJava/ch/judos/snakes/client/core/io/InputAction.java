package ch.judos.snakes.client.core.io;

import static java.awt.event.KeyEvent.*;

import java.security.InvalidParameterException;

public enum InputAction implements InputActionI {

	// MOVEMENT
	PAN_LEFT(VK_A, VK_LEFT),
	PAN_RIGHT(VK_D, VK_RIGHT),
	PAN_UP(VK_W, VK_UP),
	PAN_DOWN(VK_S, VK_DOWN),
	MOUSE_ZOOM(InputController.MOUSE_SCROLL),

	// TOOLS
	DROP_TOOL(VK_ESCAPE),
	BT_BUILD_TILE(InputController.MOUSE_LEFT),
	BT_DELETE_TILE(InputController.MOUSE_RIGHT),
	BT_ROTATE_LEFT(VK_Q),
	BT_ROTATE_RIGHT(VK_E),
	BT_MIRROR(VK_TAB),

	// SIGNAL TOOL
	ST_BUILD_SIGNAL(InputController.MOUSE_LEFT),
	ST_DELETE_SIGNAL(InputController.MOUSE_RIGHT),

	// TOOLBAR
	TOOLBAR_0(VK_MINUS, 16777383), // "§" left of number 1
	TOOLBAR_1(VK_1),
	TOOLBAR_2(VK_2),
	TOOLBAR_3(VK_3),
	TOOLBAR_4(VK_4),
	TOOLBAR_5(VK_5),
	TOOLBAR_6(VK_6),
	TOOLBAR_7(VK_7),
	TOOLBAR_8(VK_8),
	TOOLBAR_9(VK_9),
	TOOLBAR_10(VK_0),

	// GENERAL
	SELECT(InputController.MOUSE_LEFT),
	DESELECT(VK_ESCAPE),
	SELECT2(InputController.MOUSE_RIGHT),
	INPUT_GUIDE(VK_F1),
	DEBUG_TOOL(VK_F2),
	TAKE_SCREENSHOT(VK_F3),
	PROFILER(VK_F6),
	QUIT_GAME(VK_F12),
	BACKSPACE(VK_BACK_SPACE),
	LEFT(VK_LEFT),
	RIGHT(VK_RIGHT),
	DELETE(VK_DELETE),
	HOME(VK_HOME),
	END(VK_END),
	CONFIRM(VK_ENTER),;

	public static InputAction getToolbarActionByNumber(int number) {
		if (number < 0 || number > 10)
			throw new InvalidParameterException("number must be >= 0 && <= 10");
		return InputAction.valueOf("TOOLBAR_" + number);
	}

	/**
	 * possible keycodes to execute this action
	 */
	public final int[] keycodes;

	InputAction(int... defaultKeycodes) {
		this.keycodes = defaultKeycodes;
	}

	@Override
	public int[] getKeyCodes() {
		return keycodes;
	}

	public boolean isActivatedBy(int keyCode) {
		for (int possibleKeyCode : this.keycodes) {
			if (keyCode == possibleKeyCode)
				return true;
		}
		return false;
	}
}
