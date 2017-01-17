package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.stream.Stream;

import ch.judos.generic.data.HashMapList;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.graphics.Drawable2d;
import controller.game.CollisionController;
import controller.game.PlayerControls;
import model.Map;
import model.game.DrawingLayer;
import view.game.SnakeDrawer;

/**
 * @author Julian Schelker
 */
public class MapDrawer implements Drawable2d {

	private Map map;
	private SnakeDrawer snakeDrawer;
	private PlayerControls controller;
	public HashMapList<DrawingLayer, Drawable2d> drawables;
	public Drawable2d[] drawableRuntime;
	private static Font text = new Font("Arial", 0, 24);

	public MapDrawer(Map map) {
		this.map = map;
		this.drawables = new HashMapList<DrawingLayer, Drawable2d>();
		this.snakeDrawer = new SnakeDrawer(map);
	}

	public void setController(PlayerControls controller) {
		this.controller = controller;
	}

	public void initializeForDrawing() {
		this.drawableRuntime = Stream.of(DrawingLayer.values()).sequential().map(
			layer -> this.drawables.getList(layer)).filter(list -> list != null).flatMap(
				Collection::stream).toArray(length -> new Drawable2d[length]);
	}

	@Override
	public void paint(Graphics2D g) {
		AffineTransform originalTransformation = g.getTransform();

		for (Drawable2d d : this.drawableRuntime) {
			d.paint(g);
		}

		PointF center = this.controller.getFocusPoint();
		int grid = CollisionController.gridSize;
		int lines = g.getClipBounds().width / grid;
		g.setColor(Color.gray);
		int offsetX = center.getXI() % grid;
		int offsetY = center.getYI() % grid;
		for (int i = 0; i <= lines; i++) {
			g.fillRect(i * grid - offsetX, 0, 1, 1080);
			g.fillRect(0, i * grid - offsetY, 1920, 1);
		}
		g.translate(-center.getXI(), -center.getYI());
		this.snakeDrawer.drawAllSnakes(g);

		g.setTransform(originalTransformation);
		g.setColor(Color.white);
		Rectangle bounds = g.getClipBounds();
		g.fillOval(bounds.width / 2 - 2, bounds.height / 2 - 2, 4, 4);
		g.setFont(text);
		g.drawString("some text ", 20, 20);
	}

}
