package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.*
import ch.judos.snakes.client.core.window.GameWindow
import ch.judos.snakes.client.model.ClientSettings

class MenuScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow,
		private val clientSettings: ClientSettings
) : BasicScene(sceneController, inputController, window) {

	init {
		val panel = BasePanel()
		panel.add(Label("Welcome " + this.clientSettings.name, true))
		panel.add(Spacer(1,100))

		val lobbyPlayerPanel = BasePanel(isVertical = false)
		val lobbyPanel = BasePanel()
		lobbyPlayerPanel.add(lobbyPanel)
		lobbyPlayerPanel.add(Spacer(100,1))
		val playerPanel = BasePanel()
		lobbyPlayerPanel.add(playerPanel)
		panel.add(lobbyPlayerPanel)
		panel.add(Spacer(1,100))

		val buttonPanel = BasePanel(isVertical = false)
		buttonPanel.add(Button("Create New Game", inputController) {
			// TODO: implement
		})
		buttonPanel.add(Spacer(100,1))
		buttonPanel.add(Button("Join Game", inputController) {
			// TODO:
		})
		buttonPanel.add(Spacer(100,1))
		buttonPanel.add(Button("Quit", inputController) {
			this.sceneController.quit()
		})

		val view = WindowComponent(Design.titleFont, this.inputController)
		view.isHeadless = true
		view.isMovable = false
		view.addComponent(panel)
		this.ui.addWindow(view)
	}

}
