package scene.menu

import core.base.Design
import core.input.InputController
import core.ui.Button
import core.ui.WindowComponent
import model.ClientSettings

class MainMenuView(
		private val input: InputController,
		private val clientSettings: ClientSettings
) : WindowComponent(
		Design.titleFont, input
) {

	init {
		this.title = "Welcome " + this.clientSettings.name

		addComponent(Button("Exit Game", input, this::dispose)).setWeight(1, 0)
	}

}