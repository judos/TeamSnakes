package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.BasePanel
import ch.judos.snakes.client.core.ui.Button
import ch.judos.snakes.client.core.ui.InputField
import ch.judos.snakes.client.core.ui.WindowComponent
import ch.judos.snakes.client.core.window.GameWindow
import ch.judos.snakes.client.model.GameData

class LoginScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow,
		private val gameData: GameData
) : BasicScene(sceneController, inputController, window) {

	var inputField: InputField

	init {
		val view = WindowComponent(Design.titleFont).apply {
			this.title = "Enter your player name"
			inputField = InputField(30, inputController)
			inputField.focus()
			addComponent(inputField)

			val panel = BasePanel(isVertical = false)
			panel.add(Button("Ok") {
				nameEntered()
			})
			panel.add(Button("Exit") {
				sceneController.quit()
			})
			addComponent(panel)
		}
		this.ui.addWindow(view)
	}

	private fun nameEntered() {
		if (inputField.text.isNullOrEmpty())
			return
		this.gameData.settings.name = inputField.text
		this.sceneController.loadScene(LoadingScene::class.java)
	}

}
