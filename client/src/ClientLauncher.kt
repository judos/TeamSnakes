import controller.GameController
import controller.NetworkController
import core.base.Controller
import core.base.SceneFactory
import core.input.InputController
import core.window.GameWindow
import core.window.ResizableWindow
import model.ClientConfig
import model.ClientSettings
import org.apache.logging.log4j.LogManager
import scene.menu.LoadingScene
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
		val settings = ClientSettings()

		sceneFactory.register(LoadingScene::class.java) { LoadingScene(controller, input, window) }
		sceneFactory.register(LoginScene::class.java) { LoginScene(controller, input, window, settings) }
		sceneFactory.register(MenuScene::class.java) { MenuScene(controller, input, window, settings) }

		controller.start()
		controller.loadScene(LoadingScene::class.java)
		view.onClosed = Runnable { controller.quit() }

		val networkController = NetworkController(settings, config, )
		val gameController = GameController(controller, settings, networkController)
	}
}

fun main() {
	System.setProperty("sun.java2d.opengl", "True")
	System.setProperty("sun.java2d.accthreshold", "0")
	val launcher = ClientLauncher()
	launcher.init()
}
