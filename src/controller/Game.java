package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import controller.game.PlayerControls;
import controller.game.SnakeController;
import model.Map;
import model.game.Options;
import view.Gui;
import view.MapDrawer;

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

	private Options options;

	private MapDrawer mapDrawer;

	public Game(Map m, Gui gui) {
		this.map = m;
		this.gui = gui;
		this.gameIsPaused = false;

		this.options = new Options();
		this.synchronousEventController = new SynchronousEventController(this.gui
			.getInputProvider());
		this.mapDrawer = new MapDrawer(this.map);
		this.gui.setDrawable(this.mapDrawer);

		this.mouseController = new MouseController(this.map, gui.getComponent());
		this.snakeController = new SnakeController(this.map);
		this.playerControls = new PlayerControls(this.map.snakes.get(0), this.mouseController);
		this.mapDrawer.setController(this.playerControls);

		this.synchronousEventController.addMouseListener(this.mouseController);
		this.synchronousEventController.addKeyListener(this);

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
		if (e.getKeyChar() == KeyEvent.VK_F5)
			this.options.toggleDebugOptions();
	}

}
