import core.base.Controller
import core.base.SceneFactory
import core.input.InputController
import core.window.GameWindow
import core.window.ResizableWindow
import model.ClientConfig
import model.ClientSettings
import org.apache.logging.log4j.LogManager
import scene.menu.LoginScene
import scene.menu.MenuScene


class ClientLauncher {

	private val logger = LogManager.getLogger(javaClass)!!

	fun init() {
		val config = ClientConfig.load()
		val input = InputController()
		//		GameWindow window = new NativeFullscreen(input);
		val view = ResizableWindow("TeamSnakes", 1650, 1040, input)
		val window: GameWindow = view
		val sceneFactory = SceneFactory()
		val controller = Controller(window, sceneFactory)
		val clientConfig = ClientSettings()

		sceneFactory.register(LoginScene::class.java) { LoginScene(controller, input, window, clientConfig) }
		sceneFactory.register(MenuScene::class.java) { MenuScene(controller, input, window, clientConfig) }

		controller.start()
		controller.loadScene(LoginScene::class.java)
		view.onClosed = Runnable { controller.quit() }
	}
}

fun main() {
	System.setProperty("sun.java2d.opengl", "True")
	System.setProperty("sun.java2d.accthreshold", "0")
	val launcher = ClientLauncher()
	launcher.init()
}
