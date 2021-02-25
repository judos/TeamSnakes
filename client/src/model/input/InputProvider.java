package model.input;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;

/**
 * @author Julian Schelker
 */
public interface InputProvider {
	public void addMouseListener(MouseListener m);

	public void addKeyListener(KeyListener k);

	public void addMouseWheelListener(MouseWheelListener m);
}
