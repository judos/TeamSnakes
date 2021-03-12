package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.WindowComponent
import ch.judos.snakes.client.core.window.GameWindow

class LobbyScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow
) : BasicScene(sceneController, inputController, window) {

	private lateinit var view: WindowComponent

	override fun loadScene() {
		this.view = WindowComponent(Design.titleFont).apply {
			this.title = "Game Lobby"
		}

		this.ui.addWindow(view)
	}


}