package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.Button
import ch.judos.snakes.client.core.ui.WindowComponent
import ch.judos.snakes.client.core.window.GameWindow
import ch.judos.snakes.client.model.ClientSettings

class MenuScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow,
		private val clientSettings: ClientSettings
) : BasicScene(sceneController, inputController, window) {

	init {
		val view = WindowComponent(Design.titleFont, this.inputController)
		view.title = "Welcome " + this.clientSettings.name
		view.addComponent(Button("Exit Game", inputController) {
			this.sceneController.quit()
		}.setWeight(1, 0))
		this.ui.addWindow(view)
	}

}
