package core.base;

import core.input.InputController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class SceneFactory {
	protected final Logger logger = LogManager.getLogger();

	private Window window;
	private SceneController controller;

	private InputController input;

	public SceneFactory(SceneController controller, Window window, InputController input) {
		this.controller = controller;
		this.window = window;
		this.input = input;
	}

	public Scene createScene(Class<? extends Scene> sceneClass, Object... args) {
		HashMap<Class<?>, Object> mapArgs = mapArguments(args);
		// TODO: delegate to scene classes
		logger.error("Can't create scene of type {}", sceneClass);
		return null;
	}

	private HashMap<Class<?>, Object> mapArguments(Object[] args) {
		HashMap<Class<?>, Object> result = new HashMap<>();
		for (Object arg : args)
			result.put(arg.getClass(), arg);
		return result;
	}
}
