package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import model.Map;
import view.game.SnakeDrawer;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @author Julian Schelker
 */
public class MapDrawer implements Drawable2d {

	private Map map;
	private SnakeDrawer snakeDrawer;
	private static Font text = new Font("Arial", 0, 24);

	public MapDrawer(Map map) {
		this.map = map;

		this.snakeDrawer = new SnakeDrawer(map);
	}

	@Override
	public void paint(Graphics2D g) {
		g.fillRect(0, 0, 1920, 1080);

		this.snakeDrawer.drawAllSnakes(g);

		g.setColor(Color.white);
		g.setFont(text);
		g.drawString("some text ", 20, 20);
	}

}
