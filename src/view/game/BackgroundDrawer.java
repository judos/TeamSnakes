package view.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import ch.judos.generic.data.HashMapList;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.graphics.Drawable2d;
import controller.GameI;
import controller.game.CollisionController;
import controller.game.PlayerControls;
import model.game.Options;
import model.game.SnakeTile;
import model.game.helper.GridHashing;
import view.Assets;

public class BackgroundDrawer implements Drawable2d {

	private Options options;
	private PlayerControls controller;
	private HashMapList<Integer, SnakeTile> hashMap;

	public BackgroundDrawer(GameI game, HashMapList<Integer, SnakeTile> snakeCollisionMap) {
		this.options = game.getOptions();
		this.controller = game.getControls();
		this.hashMap = snakeCollisionMap;
	}

	@Override
	public void paint(Graphics2D g) {
		PointF center = this.controller.getFocusPoint();
		int grid = CollisionController.gridSize;
		int gridsWidth = g.getClipBounds().width / grid;
		int gridsHeight = g.getClipBounds().height / grid;
		int gridOffsetX = center.getXI() / grid;
		int gridOffsetY = center.getYI() / grid;
		g.setColor(Color.gray);
		for (int x = -gridsWidth / 2 - 1; x <= gridsWidth / 2 + 1; x++) {
			g.fillRect((x + gridOffsetX) * grid, center.getYI() - 540, 1, 1080);
		}
		for (int y = -gridsHeight / 2 - 1; y <= gridsHeight / 2 + 1; y++) {
			g.fillRect(center.getXI() - 960, (y + gridOffsetY) * grid, 1920, 1);
		}

		if (!this.options.isDebuggingEnabled())
			return;
		g.setColor(Color.WHITE);
		g.setFont(Assets.font);
		for (int x = -gridsWidth / 2 - 1; x <= gridsWidth / 2 + 1; x++) {
			for (int y = -gridsHeight / 2 - 1; y <= gridsHeight / 2 + 1; y++) {
				int px = (x + gridOffsetX) * grid + 15;
				int py = (y + gridOffsetY) * grid + 15;
				int gx = x + gridOffsetX;
				int gy = y + gridOffsetY;
				g.drawString(gx + "/" + gy, px, py);
				ArrayList<SnakeTile> tiles = this.hashMap.getList(GridHashing.hashPointIntoMap(
					new PointI(gx, gy)));
				String t = "";
				if (tiles != null)
					t = "T: " + tiles.size();
				g.drawString(t, px, py + 15);
			}
		}
	}

}
