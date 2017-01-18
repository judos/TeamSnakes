package view.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;
import model.game.Map;
import model.game.Snake;

public class SnakeDrawer {

	private Map map;
	private Snake currentSnake;

	public SnakeDrawer(Map map) {
		this.map = map;
	}

	public void drawAllSnakes(Graphics2D g) {
		for (Snake s : this.map.snakes) {
			drawSnake(s, g);
		}
	}

	private void drawSnake(Snake snake, Graphics2D g) {
		this.currentSnake = snake;
		Color[] c = new Color[]{c(1), c(0.9f), c(0.84f), c(0.81f), c(0.84f), c(0.9f), c(1), c(
			1.1f), c(1.16f), c(1.19f), c(1.16f), c(1.1f)};

		ArrayList<PointF> points = snake.getPoints();
		int s = snake.getTileRadius();
		for (int i = points.size() - 1; i >= 0; i--) {
			g.setColor(c[i % c.length]);
			PointF point = points.get(i);
			g.fillOval(point.getXI() - s, point.getYI() - s, s * 2, s * 2);
		}
		// eyes
		g.setColor(Color.BLACK);
		PointF head = points.get(0).clone();
		Angle direction = snake.getHeadAngle();
		head.movePointI(direction.turnClockwise(Angle.A_90), s / 2);
		head.movePointI(direction, s / 3);
		g.fillOval(head.getXI() - s / 3, head.getYI() - s / 3, s / 2, s / 2);
		head.movePointI(direction.turnClockwise(Angle.A_270), s);
		g.fillOval(head.getXI() - s / 3, head.getYI() - s / 3, s / 2, s / 2);
	}

	private Color c(float scale) {
		scale /= 256;
		Color s = this.currentSnake.getColor();
		return new Color(scale * s.getRed(), scale * s.getGreen(), scale * s.getBlue());
	}

}
