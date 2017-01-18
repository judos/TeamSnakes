package controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import model.game.Map;

/**
 * @author Julian Schelker
 */
public class MouseController extends MouseAdapter {

	private Map map;
	private Component component;
	private boolean[] mouseButtonPressed;

	public MouseController(Map map, Component component) {
		this.map = map;
		this.component = component;
		this.mouseButtonPressed = new boolean[MouseInfo.getNumberOfButtons()];
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

	public void update() {
	}

}
