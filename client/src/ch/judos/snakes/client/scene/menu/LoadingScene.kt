package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.WindowComponent
import ch.judos.snakes.client.core.window.GameWindow

class LoadingScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow,
) : BasicScene(sceneController, inputController, window) {

	init {
		val view = WindowComponent(Design.titleFont, this.inputController).apply {
			this.title = "Loading..."
		}
		this.ui.addWindow(view)
	}

}
