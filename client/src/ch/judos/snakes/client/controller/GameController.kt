package ch.judos.snakes.client.controller

import ch.judos.snakes.client.core.base.Controller
import ch.judos.snakes.client.model.ClientSettings
import ch.judos.snakes.client.model.GameData
import ch.judos.snakes.client.model.LoadingData
import org.apache.logging.log4j.LogManager
import ch.judos.snakes.client.scene.menu.LoginScene
import ch.judos.snakes.client.scene.menu.MenuScene

class GameController(
		private val controller: Controller,
		private val networkController: NetworkController,
		private val gameData: GameData
) {

	private val logger = LogManager.getLogger(javaClass)!!

	init {
		if (this.gameData.settings.name == null) {
			this.controller.loadScene(LoginScene::class.java)
			this.controller.awaitSceneChange()
			logger.info("Name entered can proceed")
		}
		this.networkController.login()
		this.controller.loadScene(MenuScene::class.java)
	}

}
