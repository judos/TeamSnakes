package scene.menu

import core.base.Scene
import core.base.SceneController
import core.input.InputController
import core.input.InputEvent
import core.ui.DesktopComponent
import core.window.GameWindow

class MenuScene(
		private val sceneController: SceneController,
		private val inputController: InputController,
		private val window: GameWindow
) : Scene(sceneController) {

	private var ui: DesktopComponent

	init {
		this.ui = DesktopComponent(this.inputController)
		this.ui.layout(0,0, window.screenSize.width, window.screenSize.height)
		val view = MainMenuView(this.inputController)
		this.ui.addWindow(view)
		addRenderer(ui)
	}


	override fun handleInput(event: InputEvent?) {


	}
}
