package controller;

import ch.judos.generic.control.KeyMouseAdapter;
import model.game.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class InputController extends KeyMouseAdapter {

	private Component component;
	private boolean[] mouseButtonPressed;
	private HashMap<Integer, Boolean> keyButtonsPressed;

	public InputController(Map map, Component component) {
		this.component = component;
		this.mouseButtonPressed = new boolean[MouseInfo.getNumberOfButtons()];
		this.keyButtonsPressed = new HashMap<>();
	}
	public boolean isMouseButtonPressed(int mouseButton) {
		return this.mouseButtonPressed[mouseButton];
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.mouseButtonPressed[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.mouseButtonPressed[e.getButton()] = false;
	}

	public Point getMousePosition() {
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mousePosition, this.component);
		return mousePosition;
	}

	public Dimension getScreenSize() {
		return this.component.getSize();
	}

	public boolean isKeyPressed(int vkSpace) {
		return this.keyButtonsPressed.containsKey(vkSpace) && this.keyButtonsPressed.get(
			vkSpace);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.keyButtonsPressed.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keyButtonsPressed.put(e.getKeyCode(), false);
	}
}
