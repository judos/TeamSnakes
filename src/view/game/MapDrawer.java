package view.game;

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
import controller.game.PlayerControls;
import model.game.DrawingLayer;
import model.game.Map;

/**
 * @author Julian Schelker
 */
public class MapDrawer implements Drawable2d {

	private Map map;
	private SnakeDrawer snakeDrawer;
	private PlayerControls controller;
	public HashMapList<DrawingLayer, Drawable2d> drawables;
	private Drawable2d[] drawableRuntime;
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

		Rectangle rect = g.getClipBounds();
		g.translate(rect.width / 2, rect.height / 2);
		PointF center = this.controller.getFocusPoint();
		g.translate(-center.getXI(), -center.getYI());

		for (Drawable2d d : this.drawableRuntime) {
			d.paint(g);
		}

		g.setColor(Color.red);
		g.fillRect(0, 0, 1920, 1);
		g.fillRect(0, 0, 1, 1080);
		this.snakeDrawer.drawAllSnakes(g);

		g.setTransform(originalTransformation);
		g.setColor(Color.white);
		g.setFont(text);
		g.drawString("Size: " + this.controller.getOwnSnake().getSize(), 20, 20);
	}

}
