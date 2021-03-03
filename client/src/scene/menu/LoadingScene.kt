package scene.menu

import core.base.BasicScene
import core.base.Design
import core.base.SceneController
import core.input.InputController
import core.ui.WindowComponent
import core.window.GameWindow
import model.ClientSettings

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
