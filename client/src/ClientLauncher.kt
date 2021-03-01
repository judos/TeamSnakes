import core.base.Controller
import core.base.SceneFactory
import core.input.InputController
import core.window.GameWindow
import core.window.ResizableWindow
import model.ClientConfig
import org.apache.logging.log4j.LogManager
import scene.menu.MenuScene


class ClientLauncher {

	private val logger = LogManager.getLogger(javaClass)!!

	fun init() {
		val input = InputController()
		//		GameWindow window = new NativeFullscreen(input);
		val view = ResizableWindow("TeamSnakes", 1650, 1040, input)
		val window: GameWindow = view
		val sceneFactory = SceneFactory()
		val controller = Controller(window, sceneFactory)
		val clientConfig = ClientConfig()

		sceneFactory.register(MenuScene::class.java) { MenuScene(controller, input, window, clientConfig) }

		controller.start()
		controller.loadScene(MenuScene::class.java)
		view.onClosed = Runnable { controller.quit() }
	}
}

// TODO: remove old logging, use log4j
fun main() {
	System.setProperty("sun.java2d.opengl", "True")
	System.setProperty("sun.java2d.accthreshold", "0")
	val launcher = ClientLauncher()
	launcher.init()
}
