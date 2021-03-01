package core.base;

import ch.judos.generic.control.HighPrecisionClock;
import ch.judos.generic.graphics.ImageUtils;
import core.input.InputAction;
import core.input.InputController;
import core.input.InputEvent;
import core.profiler.Profiler;
import core.profiler.ProfilerI;
import core.window.GameWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller implements SceneController {

	private HighPrecisionClock gameClock;
	protected GameWindow window;
	protected final InputController input;
	protected Scene currentScene = null;
	protected final Logger logger = LogManager.getLogger(getClass());

	private ProfilerI profiler;
	private boolean screenshotRequested = false;

	private long startOfLastTick = System.currentTimeMillis();
	private SceneFactory sceneFactory;

	public Controller(GameWindow window,SceneFactory sceneFactory) {
		this.window = window;
		this.input = window.getInput();
		this.sceneFactory = sceneFactory;
		this.gameClock = new HighPrecisionClock(60, this::tick, "Game Loop");

		profiler = new Profiler(60);
	}

	public void start() {
		this.gameClock.start();
	}

	public void stop() {
		this.gameClock.stop();
	}

	/**
	 * Main loop of the game, rendering, updating everything
	 */
	private void tick() {

		long newStart = System.currentTimeMillis();
		long deltaT = newStart - startOfLastTick;
		startOfLastTick = newStart;

		this.profiler.startFrame();

		this.profiler.startSample(this.input);

		handleAllInputEvents();

		this.profiler.endSample(this.input);

		// setup screenshot buffers if required
		BufferedImage snapshot = null;
		Graphics2D snapshotGraphics = null;
		if (this.screenshotRequested) {
			snapshot = new BufferedImage(window.getScreenSize().width, window.getScreenSize().height, BufferedImage.TYPE_INT_RGB);
			snapshotGraphics = snapshot.createGraphics();
			snapshotGraphics.setBackground(GameWindow.BG_COLOR);
			snapshotGraphics.clearRect(0, 0, window.getScreenSize().width, window.getScreenSize().height);
			snapshotGraphics.clipRect(0, 0, window.getScreenSize().width, window.getScreenSize().height);
		}

		window.flipFrame();
		Graphics2D graphics = window.getGraphics();

		// clip makes everyone aware of the available screen size
		graphics.clipRect(0, 0, window.getScreenSize().width, window.getScreenSize().height);
		if (this.currentScene == null) return;

		if (this.window.resized()) {
			this.currentScene.screenResized(this.window.getScreenSize());
		}

		for (BaseRenderer renderer : this.currentScene.getRenderers()) {
			profiler.startSample(renderer);
//			GraphicsStack stack = graphics.getStack();
			renderer.render(graphics);
//			graphics.setStack(stack);
			if (snapshotGraphics != null) {
//				GraphicsStack stack2 = snapshotGraphics.getStack();
				renderer.render(snapshotGraphics);
//				snapshotGraphics.setStack(stack2);
			}
			profiler.endSample(renderer);
		}

		this.currentScene.preTick(deltaT);

		for (BaseTicker ticker : this.currentScene.getTickers()) {
			profiler.startSample(ticker);
			ticker.tick();
			profiler.endSample(ticker);
		}

		this.profiler.render(graphics);
		if (snapshotGraphics != null) {
			this.profiler.render(snapshotGraphics);
		}

		if (snapshot != null) {
			snapshotGraphics.dispose();
			saveScreenshot(snapshot);
		}

		profiler.endFrame();
	}

	private void handleAllInputEvents() {
		for (InputEvent event : this.input.popAllEvents()) {
			if (event.isPressActionAndConsume(InputAction.QUIT_GAME)) {
				this.quit();
			}
			if (event.isPressActionAndConsume(InputAction.TAKE_SCREENSHOT)) {
				requestScreenshot();
				continue;
			}
			this.input.handleInput(event);
			if (event.isConsumed) continue;
			this.profiler.handleInput(event);
			if (event.isConsumed) continue;
			this.currentScene.handleInput(event);
			if (event.isConsumed) continue;
			if (event.isPress()) logger.debug("unhandled: " + event);
		}
	}

	@Override
	public void quit() {
		this.gameClock.stop();
		this.window.discard();
		try {
			Thread.sleep(100);
		} catch (InterruptedException ignored) {
		}
		logger.info("Quit");
		System.exit(0);
	}

	private void saveScreenshot(Image screenshot) {
		ImageWriter writer = null;
		FileImageOutputStream outputStream = null;
		try {
			// bufferedImage
			BufferedImage buffer = ImageUtils.toBufferedImage(screenshot);

			// define screenshot quality
			JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
			jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			jpegParams.setCompressionQuality(0.95f);

			writer = ImageIO.getImageWritersByFormatName("jpg").next();
			outputStream = new FileImageOutputStream(new File("screenshot.jpg"));
			writer.setOutput(outputStream);
			// writes the file with given compression level
			// from your JPEGImageWriteParam instance
			writer.write(null, new IIOImage(buffer, null, null), jpegParams);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) writer.dispose();
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ignored) {
				}
			}
		}
		this.screenshotRequested = false;
	}

	@Override
	public Scene loadScene(Class<? extends Scene> sceneClass) {
		Scene scene = this.sceneFactory.createScene(sceneClass);
		if (scene != null) {
			if (this.currentScene != null) {
				logger.trace("unloading scene " + this.currentScene);
				this.currentScene.unloadScene();
			}

			logger.trace("loading scene " + scene);
			scene.loadScene();
			this.currentScene = scene;
		}
		return this.currentScene;
	}

	@Override
	public void requestScreenshot() {
		this.screenshotRequested = true;
	}

}
