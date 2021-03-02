package scene.menu

import core.base.BasicScene
import core.base.Design
import core.base.SceneController
import core.input.InputController
import core.ui.BasePanel
import core.ui.Button
import core.ui.InputField
import core.ui.WindowComponent
import core.window.GameWindow
import model.ClientSettings

class LoginScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow,
		private val clientSettings: ClientSettings
) : BasicScene(sceneController, inputController, window) {

	var inputField: InputField

	init {
		val view = WindowComponent(Design.titleFont, this.inputController).apply {
			this.title = "Enter your player name"
			inputField = InputField(30, inputController)
			inputField.focus()
			addComponent(inputField)

			val panel = BasePanel(isVertical = false)
			panel.add(Button("Ok", inputController) {
				nameEntered()
			})
			panel.add(Button("Exit", inputController) {
				sceneController.quit()
			})
			addComponent(panel)
		}
		this.ui.addWindow(view)
	}

	private fun nameEntered() {
		if (inputField.text.isNullOrEmpty())
			this.sceneController.quit()
		this.clientSettings.name = inputField.text
		this.sceneController.loadScene(MenuScene::class.java)
	}

}
