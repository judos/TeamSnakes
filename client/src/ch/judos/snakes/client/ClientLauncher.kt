package ch.judos.snakes.client

import ch.judos.snakes.client.controller.GameController
import ch.judos.snakes.client.controller.NetworkController
import ch.judos.snakes.client.core.base.Controller
import ch.judos.snakes.client.core.base.SceneFactory
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.window.ResizableWindow
import ch.judos.snakes.client.model.ClientConfig
import ch.judos.snakes.client.model.ClientSettings
import ch.judos.snakes.client.model.LoadingData
import ch.judos.snakes.client.scene.menu.LoadingScene
import ch.judos.snakes.client.scene.menu.LoginScene
import ch.judos.snakes.client.scene.menu.MenuScene
import ch.judos.snakes.client.scene.menu.TestingScene
import org.apache.logging.log4j.LogManager


class ClientLauncher {

	private val logger = LogManager.getLogger(javaClass)!!

	fun testing() {
		val input = InputController()
		val window = ResizableWindow("TeamSnakes", 800, 600, input)
		val sceneFactory = SceneFactory()
		val controller = Controller(window, sceneFactory)
		sceneFactory.register(TestingScene::class.java) { TestingScene(controller, input, window) }

		controller.start()
		controller.loadScene(TestingScene::class.java)
		window.onClosed = Runnable { controller.quit() }
	}

	fun init() {
		val config = ClientConfig.load()
		val input = InputController()
		//		GameWindow window = new NativeFullscreen(input);
		val window = ResizableWindow("TeamSnakes", 1650, 1040, input)
		val sceneFactory = SceneFactory()
		val controller = Controller(window, sceneFactory)
		val settings = ClientSettings()
		val loadingData = LoadingData()

		sceneFactory.register(LoadingScene::class.java) { LoadingScene(controller, input, window, loadingData) }
		sceneFactory.register(LoginScene::class.java) { LoginScene(controller, input, window, settings) }
		sceneFactory.register(MenuScene::class.java) { MenuScene(controller, input, window, settings) }

		loadingData.set("Loading Game")
		controller.start()
		controller.loadScene(LoadingScene::class.java)
		window.onClosed = Runnable { controller.quit() }

		val networkController = NetworkController(settings, config, loadingData)
		val gameController = GameController(controller, settings, networkController, loadingData)
	}
}

fun main() {
	System.setProperty("sun.java2d.opengl", "True")
	System.setProperty("sun.java2d.accthreshold", "0")
	val launcher = ClientLauncher()
//	launcher.init(
	launcher.testing()
}
