package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.Label
import ch.judos.snakes.client.core.ui.WindowComponent
import ch.judos.snakes.client.core.window.GameWindow
import ch.judos.snakes.client.model.LoadingData
import java.util.function.Consumer

class LoadingScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow,
		private var loadingData: LoadingData,
) : BasicScene(sceneController, inputController, window) {

	private var view: WindowComponent? = null
	private var consumer: Consumer<LoadingData>

	init {
		this.consumer = Consumer<LoadingData> {
			update(it)
		}
		this.loadingData.subscribers.add(this.consumer)
		update(loadingData)
	}

	private fun update(it: LoadingData) {
		this.view?.dispose()
		this.view = WindowComponent(Design.titleFont).apply {
			this.title = "Loading..."
		}
		for (str in it.current) {
			val label = Label(str).setWeight(1, 1)
			view!!.addComponent(label)
		}
		this.ui.addWindow(view)
	}

	override fun unloadScene() {
		this.loadingData.subscribers.remove(this.consumer)
	}

}
