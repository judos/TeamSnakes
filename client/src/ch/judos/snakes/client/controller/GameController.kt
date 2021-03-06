package ch.judos.snakes.client.controller

import ch.judos.snakes.client.core.base.Controller
import ch.judos.snakes.client.model.ClientSettings
import org.apache.logging.log4j.LogManager
import ch.judos.snakes.client.scene.menu.LoginScene
import ch.judos.snakes.client.scene.menu.MenuScene

class GameController(
		private val controller: Controller,
		private val clientSettings: ClientSettings,
		private val networkController: NetworkController
) {

	private val logger = LogManager.getLogger(javaClass)!!

	init {
		if (this.clientSettings.name == null) {
			this.controller.loadScene(LoginScene::class.java)
			this.controller.awaitSceneChange()
			logger.info("Name entered can proceed")
		}
		this.networkController.login()
		this.controller.loadScene(MenuScene::class.java)
	}

}