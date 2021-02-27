import ch.judos.generic.control.Log
import core.base.Controller
import core.base.SceneFactory
import core.input.InputController
import core.window.GameWindow
import core.window.ResizableWindow
import scene.menu.MenuScene


class Launcher {
	fun init() {
		val input = InputController()
		//		GameWindow window = new NativeFullscreen(input);
		val view = ResizableWindow("TeamSnakes", 1650, 1040, input)
		val window: GameWindow = view
		val sceneFactory = SceneFactory()
		val controller = Controller(window, sceneFactory)
		sceneFactory.register(MenuScene::class.java) { MenuScene(controller, input, window) }

		controller.start()
		controller.loadScene(MenuScene::class.java)
		view.onClosed = Runnable { controller.quit() }
	}
}

fun main() {
	Log.getInstance().logToFile = false
	Log.getInstance().currentLogLevel = Log.Level.INFO
	System.setProperty("sun.java2d.opengl", "True")
	System.setProperty("sun.java2d.accthreshold", "0")
	val launcher = Launcher()
	launcher.init()
}
