package controller

import core.base.Controller
import model.ClientSettings
import org.apache.logging.log4j.LogManager
import scene.menu.LoginScene

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


	}

}