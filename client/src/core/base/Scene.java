package core.base;

import core.input.InputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene implements InputHandler {

	protected final Logger logger = LogManager.getLogger(getClass());
	
	protected final SceneController sceneController;

	private final List<BaseRenderer> renderers;
	private final List<BaseTicker> tickers;

	public Scene(SceneController sceneController) {
		this.sceneController = sceneController;

		this.tickers = new ArrayList<>();
		this.renderers = new ArrayList<>();
	}

	protected void addRenderer(BaseRenderer component) {
		renderers.add(component);
	}

	protected void addTicker(BaseTicker component) {
		tickers.add(component);
	}

	public String getSceneName() {
		return this.getClass().getSimpleName();
	}

	public List<BaseRenderer> getRenderers() {
		return renderers;
	}
	public List<BaseTicker> getTickers() {
		return tickers;
	}
	/**
	 * Notifies the scene that a tick is starting and provides the exact time that passed since the start of the last
	 * tick. This value does not necessarily have to correspond to the time between calls to this method depending on
	 * when the method is being called. It can be expected that the function is called after rendering and includes uses
	 * the times at which rendering started for the time difference. This should provide the most possible stability for
	 * this value. It is guaranteed that this method will always be called before the first ticker is invoked.
	 * 
	 * Due to the place in the tick this method is called it should be implemented with the lowest possible duration.
	 * This method is not monitored by the profiler.
	 * 
	 * The default implementation is empty. Therefore a super call is not necessary
	 * 
	 * @param millis the time since the last tick
	 */
	public void preTick(long millis) {
	}

	/**
	 * Notifies the scene that screen has been resized
	 */
	public void screenResized(Dimension size) {
	}

	public void loadScene() {
	}

	public void unloadScene() {
	}

}
