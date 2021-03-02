package core.base

import core.input.InputController
import core.input.InputEvent
import core.ui.DesktopComponent
import core.window.GameWindow
import java.awt.Dimension

abstract class BasicScene(
		sceneController: SceneController,
		protected val inputController: InputController,
		protected val window: GameWindow,
) : Scene(sceneController) {

	protected var ui: DesktopComponent = DesktopComponent(this.inputController)

	init {
		this.ui.layout(0, 0, window.screenSize.width, window.screenSize.height)
		addRenderer(ui)
	}

	override fun handleInput(event: InputEvent?) {
		this.ui.handleInput(event)
	}

	override fun screenResized(size: Dimension?) {
		this.ui.layout(0, 0, size!!.width, size.height)
	}
}