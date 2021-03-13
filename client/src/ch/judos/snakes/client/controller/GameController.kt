package ch.judos.snakes.client.controller

import ch.judos.snakes.client.core.base.Controller
import ch.judos.snakes.client.model.GameData
import ch.judos.snakes.client.scene.menu.LoadingScene
import ch.judos.snakes.client.scene.menu.LobbyScene
import ch.judos.snakes.client.scene.menu.LoginScene
import ch.judos.snakes.client.scene.menu.MenuScene
import org.apache.logging.log4j.LogManager
import java.util.function.Consumer

class GameController(
		private val controller: Controller,
		private val networkController: NetworkController,
		private val gameData: GameData
) {

	init {
		networkController.regionConnectionLost = this::start
	}

	fun createLobby(done: Runnable) {
		val lobbyName = this.gameData.settings.name + "'s Game"
		// XXX: let user choose mode when creating lobby
		val mode = "snakes"
		this.networkController.createLobby(lobbyName, mode) {
			done.run()
		}
	}

	private val logger = LogManager.getLogger(javaClass)!!

	fun start() {
		if (this.gameData.settings.name == null) {
			this.controller.loadScene(LoginScene::class.java)
			this.controller.awaitSceneChange()
			logger.info("Name entered can proceed")
		}
		this.controller.loadSceneIfNotPresent(LoadingScene::class.java)
		this.networkController.login()
		this.controller.loadScene(MenuScene::class.java)
	}

}
