package ch.judos.snakes.client.old.view.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.snakes.client.old.model.game.Map;
import ch.judos.snakes.client.old.model.game.Snake;

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

		ArrayList<PointF> points = snake.getPoints();
		int s = snake.getTileRadius();
		Color[] c = colorRainbow();
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

	public Color[] colorRainbow() {
		double[] scales = {0.84, 0.75, 0.7, 0.68, 0.7, 0.75, 0.84, 0.92, 0.97, 1, 0.97, 0.92};
		final Color[] colors = Arrays.stream(scales).mapToObj(d -> c((float) d)).toArray(
			size -> new Color[size]);
		return colors;
	}

	private Color c(float scale) {
		scale /= 256;
		Color s = this.currentSnake.getColor();
		return new Color(scale * s.getRed(), scale * s.getGreen(), scale * s.getBlue());
	}

}
