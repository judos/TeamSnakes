package scene.menu

import core.base.BasicScene
import core.base.Design
import core.base.Scene
import core.base.SceneController
import core.input.InputController
import core.input.InputEvent
import core.ui.Button
import core.ui.DesktopComponent
import core.ui.WindowComponent
import core.window.GameWindow
import model.ClientSettings
import java.awt.Dimension
import java.util.function.Consumer

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
