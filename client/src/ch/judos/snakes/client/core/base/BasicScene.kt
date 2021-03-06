package ch.judos.snakes.client.core.base

import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.io.InputEvent
import ch.judos.snakes.client.core.ui.DesktopComponent
import ch.judos.snakes.client.core.window.GameWindow
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