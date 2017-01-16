package controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import model.Map;

/**
 * @author Julian Schelker
 */
public class MouseController extends MouseAdapter {

	private Map map;
	private Component component;

	public MouseController(Map map, Component component) {
		this.map = map;
		this.component = component;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
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
