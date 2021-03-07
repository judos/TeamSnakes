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

	private var consumer: Consumer<LoadingData>

	init {
		val view = WindowComponent(Design.titleFont, this.inputController).apply {
			this.title = "Loading..."
		}
		val label = Label("")
		label.setWeight(1, 1)
		view.addComponent(label)
		this.ui.addWindow(view)

		this.consumer = Consumer<LoadingData> {
			label.text = it.current
		}
		this.loadingData.subscribe(this.consumer)
	}

	override fun unloadScene() {
		this.loadingData.unsubscribe(this.consumer)
	}

}
