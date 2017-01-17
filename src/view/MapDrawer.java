package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import model.Map;
import view.game.SnakeDrawer;
import ch.judos.generic.graphics.Drawable2d;
import controller.game.PlayerControls;

/**
 * @author Julian Schelker
 */
public class MapDrawer implements Drawable2d {

	private Map map;
	private SnakeDrawer snakeDrawer;
	private PlayerControls controller;
	private static Font text = new Font("Arial", 0, 24);

	public MapDrawer(Map map) {
		this.map = map;
		this.snakeDrawer = new SnakeDrawer(map);
	}

	@Override
	public void paint(Graphics2D g) {

		AffineTransform originalTransformation = g.getTransform();
		this.controller.setOffset(g);
		this.snakeDrawer.drawAllSnakes(g);

		g.setTransform(originalTransformation);
		g.setColor(Color.white);
		Rectangle bounds = g.getClipBounds();
		g.fillOval(bounds.width / 2 - 2, bounds.height / 2 - 2, 4, 4);
		g.setFont(text);
		g.drawString("some text ", 20, 20);
	}

	public void setController(PlayerControls playerControls) {
		this.controller = playerControls;
	}

}
