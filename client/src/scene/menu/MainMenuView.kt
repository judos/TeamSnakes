package scene.menu

import core.base.Design
import core.input.InputController
import core.ui.Button
import core.ui.WindowComponent
import kotlin.system.exitProcess

class MainMenuView(
		private val input: InputController
) : WindowComponent(
		Design.titleFont, input
) {

	init {
		this.title = "Main Menu"

		addComponent(Button("Test", input, {}))
		addComponent(Button("Exit", input, this::exit))
	}

	fun exit() {
		exitProcess(0)
	}
}