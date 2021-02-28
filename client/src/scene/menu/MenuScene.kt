package scene.menu

import core.base.Scene
import core.base.SceneController
import core.input.InputController
import core.input.InputEvent
import core.ui.DesktopComponent
import core.window.GameWindow
import model.ClientConfig
import java.awt.Dimension
import java.util.function.Consumer

class MenuScene(
		private val sceneController: SceneController,
		private val inputController: InputController,
		private val window: GameWindow,
		private val clientConfig: ClientConfig
) : Scene(sceneController) {

	private var ui: DesktopComponent

	init {
		this.ui = DesktopComponent(this.inputController)
		this.ui.layout(0, 0, window.screenSize.width, window.screenSize.height)
		if (clientConfig.name == null) {
			this.loadNameView()
		} else {
			this.loadMainView()
		}
		addRenderer(ui)
	}

	private fun loadNameView() {
		val name = MainEnterNameView(this.inputController)
		name.onClose = Consumer {
			if (it.isNullOrEmpty())
				this.sceneController.quit()
			this.clientConfig.name = it
			loadMainView()
		}
		this.ui.addWindow(name)
	}

	private fun loadMainView() {
		val view = MainMenuView(this.inputController, this.clientConfig)
		view.onClose = Consumer {
			this.sceneController.quit()
		}
		this.ui.addWindow(view)
	}


	override fun handleInput(event: InputEvent?) {
		this.ui.handleInput(event)
	}

	override fun screenResized(size: Dimension?) {
		this.ui.layout(0, 0, size!!.width, size.height)
	}
}
