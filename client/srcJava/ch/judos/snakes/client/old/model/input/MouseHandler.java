package ch.judos.snakes.client.old.model.input;

/**
 * @author Julian Schelker
 */
public interface MouseHandler {

	/**
	 * @param m
	 * @return if this returns true, other mouseHandlers are usually not taken
	 *         into account anymore
	 */
	public boolean handles(MouseEvent2 m);
}
