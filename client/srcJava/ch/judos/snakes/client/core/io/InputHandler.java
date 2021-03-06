package ch.judos.snakes.client.core.io;

public interface InputHandler {

	/**
	 * test if you should, can and want handle the given inputEvent. If yes call consume() on it.
	 * 
	 * @param event
	 */
	public void handleInput(InputEvent event);

	/**
	 * tell the object that it was unfocused, this does not mean that the InputController knows that the component is no
	 * longer focused. Usually this is called by the InputController on a deselect event
	 */
	public default void unfocus() {
	}

	/**
	 * tell the object that it was focused, this does not mean that the InputController knows that the component is now
	 * focused.
	 */
	public default void focus() {
	}
}
