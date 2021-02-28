package scene.menu

import core.base.Design
import core.input.InputController
import core.ui.Button
import core.ui.WindowComponent
import model.ClientConfig

class MainMenuView(
		private val input: InputController,
		private val clientConfig: ClientConfig
) : WindowComponent(
		Design.titleFont, input
) {

	init {
		this.title = "Welcome " + this.clientConfig.name

		addComponent(Button("Exit Game", input, this::dispose))
	}

}