import java.util.Optional;

import controller.Game;
import model.game.Map;
import model.game.MapGenerator;
import view.Assets;
import view.Gui;

/**
 * @author Julian Schelker
 */
public class Launcher {
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");

		// Log.setLevel(Log.Level.INFO);

		Launcher launcher = new Launcher();
		launcher.init();
	}

	void init() {
		Assets.load();
		Map map = MapGenerator.getMap();
		Optional<Runnable> shutdown = Optional.of(() -> System.exit(0));
		Gui gui = new Gui(shutdown);

		Game game = new Game(map, gui);
		Assets.waitUntilAssetsAreLoaded();
		game.start();
	}
}
