package view.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import model.Map;
import model.game.Snake;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;

public class SnakeDrawer {

	private Map map;

	public SnakeDrawer(Map map) {
		this.map = map;
	}

	public void drawAllSnakes(Graphics2D g) {
		AffineTransform originalTransform = g.getTransform();
		Rectangle rect = g.getClipBounds();
		g.translate(rect.width / 2, rect.height / 2);
		for (Snake s : this.map.snakes) {
			drawSnake(s, g);
		}
		g.setTransform(originalTransform);
	}

	private void drawSnake(Snake snake, Graphics2D g) {
		Color[] c = new Color[]{snake.getColor(), snake.getColor().darker()};

		ArrayList<PointF> points = snake.getPoints();
		int s = (int) Snake.circleSize;
		for (int i = points.size() - 1; i >= 0; i--) {
			g.setColor(c[i % 2]);
			PointF point = points.get(i);
			g.fillOval(point.getXI() - s / 2, point.getYI() - s / 2, s, s);
		}
		// eyes
		g.setColor(Color.BLACK);
		PointF head = points.get(0).clone();
		head.movePointI(snake.headAngle.turnClockwise(Angle.A_90), s / 4);
		head.movePointI(snake.headAngle, s / 6);
		g.fillOval(head.getXI() - s / 6, head.getYI() - s / 6, s / 3, s / 3);
		head.movePointI(snake.headAngle.turnClockwise(Angle.A_270), s / 2);
		g.fillOval(head.getXI() - s / 6, head.getYI() - s / 6, s / 3, s / 3);
	}

}
