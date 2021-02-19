package view.game;

import java.awt.Color;
import java.awt.Graphics2D;

import ch.judos.generic.control.ActionCounter;
import ch.judos.generic.control.Log;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.graphics.Drawable2d;
import controller.GameI;
import model.game.Map;

public class EatablePointDrawer implements Drawable2d {

	private GameI game;
	private Map map;
	private ActionCounter loop;

	public EatablePointDrawer(GameI game) {
		this.game = game;
		this.map = game.getMap();
		this.loop = new ActionCounter(500);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.WHITE);

		PointI x = new PointI();
		this.map.eatablePoints.forEachInRect(g.getClipBounds(), eatablePoint -> {
			int s = eatablePoint.getSize();
			PointI pos = eatablePoint.getLocation();
			g.fillOval(pos.x - (s << 2), pos.y - (s << 2), s << 3, s << 3);
			x.x++;
			return false;
		});
		if (this.loop.action()) {
			Log.verbose("Points drawn: " + x + " clip: " + g.getClipBounds());
		}
	}

}