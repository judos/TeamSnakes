package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.Map;
import view.Gui;
import controller.game.PlayerControls;
import controller.game.SnakeController;

/**
 * @author Julian Schelker
 */
public class Game extends KeyAdapter {

	public static final int FPS = 60;

	private Map map;
	private Gui gui;
	private MouseController mouseController;
	private boolean gameIsPaused;
	private double gameTime;

	private SynchronousEventController synchronousEventController;

	private SnakeController snakeController;

	private PlayerControls playerControls;

	private static Game instance;

	public synchronized static Game initializeGame(Map m, Gui gui) {
		if (instance != null)
			throw new RuntimeException("Game can only be initialized once");
		return instance = new Game(m, gui);
	}

	public static double getGameTime() {
		if (instance == null)
			return 0;
		return instance.gameTime;
	}

	private Game(Map m, Gui gui) {
		this.map = m;
		this.gui = gui;
		this.gameIsPaused = false;

		this.synchronousEventController = new SynchronousEventController(this.gui
			.getInputProvider());

		this.mouseController = new MouseController(this.map, gui.getComponent());
		this.snakeController = new SnakeController(this.map);
		this.playerControls = new PlayerControls(this.map.snakes.get(0), this.mouseController);
		this.map.getMapDrawer().setController(this.playerControls);

		this.synchronousEventController.addMouseListener(this.mouseController);
		this.synchronousEventController.addKeyListener(this);

		this.gameTime = 0d;
	}

	private void pauseGame() {
		this.gameIsPaused = true;
	}

	public void start() {
		this.gui.setDrawable(this.map.getMapDrawer());
		// this.gui.startFullScreen(FPS);
		// this.gui.startViewUndecorated(FPS);
		this.gui.startView(FPS);
		this.gui.setUpdate(this::updateBeforeDrawing);
	}

	private void updateBeforeDrawing() {
		this.synchronousEventController.distributeEvents();
		if (!this.gameIsPaused) {
			this.mouseController.update();
			this.playerControls.update();
			this.snakeController.update();

			this.gameTime += 1. / FPS;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.gui.quit();
	}

}
