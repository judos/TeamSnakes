package controller;

import controller.game.PlayerControls;
import controller.game.PointController;
import controller.game.SnakeController;
import core.input.InputController;
import model.game.Map;
import model.game.Options;
import view.Gui;
import view.game.MapDrawer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game extends KeyAdapter implements GameI {

	public static final int FPS = 60;

	private Map map;
	private Gui gui;
	private InputController inputController;
	private boolean gameIsPaused;
	private double gameTime;

	private SnakeController snakeController;

	private PlayerControls playerControls;

	private Options options;

	private MapDrawer mapDrawer;

	private PointController pointController;

	public Game(Map m, Gui gui) {
		this.map = m;
		this.gui = gui;
		this.gameIsPaused = false;

		this.options = new Options();
		this.mapDrawer = new MapDrawer(this.map);
		this.gui.setDrawable(this.mapDrawer);

		this.inputController = new InputController();

		this.playerControls = new PlayerControls(this.map.snakes.get(0), this.inputController);
		this.pointController = new PointController(this);
		this.snakeController = new SnakeController(this);
		this.mapDrawer.setController(this.playerControls);

		this.gameTime = 0d;
	}

	private void pauseGame() {
		this.gameIsPaused = true;
	}

	public void start() {
		this.mapDrawer.initializeForDrawing();
		// this.gui.startFullScreen(FPS);
		// this.gui.startViewUndecorated(FPS);
		this.gui.startView(FPS);
		this.gui.setUpdate(this::updateBeforeDrawing);
	}

	private void updateBeforeDrawing() {
//		this.synchronousEventController.distributeEvents();
		if (!this.gameIsPaused) {
			this.playerControls.update();
			this.snakeController.update();
			this.pointController.update();

			this.gameTime += 1. / FPS;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.gui.quit();
		if (e.getKeyCode() == KeyEvent.VK_F5)
			this.options.toggleDebugOptions();
	}

	@Override
	public MapDrawer getMapDrawer() {
		return this.mapDrawer;
	}

	@Override
	public Map getMap() {
		return this.map;
	}

	@Override
	public Options getOptions() {
		return this.options;
	}

	@Override
	public PlayerControls getControls() {
		return this.playerControls;
	}

}
