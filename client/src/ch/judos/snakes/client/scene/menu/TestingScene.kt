package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.*
import ch.judos.snakes.client.core.window.GameWindow
import java.awt.Dimension

class TestingScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow
) : BasicScene(sceneController, inputController, window) {

	private val list: MutableList<String> = mutableListOf()
	private val slist: SelectableList<String> = SelectableList(this.list, inputController)

	init {
		val view = WindowComponent(Design.titleFont)
		view.title = "Testing Scene"
		view.isHeadless = true

		val basePanel = BasePanel()
		basePanel.margin = 40
		basePanel.padding = 40

		this.slist.setWeight(1, 0)
		val scroll = ScrollPanel(Dimension(100, 200))
		scroll.add(slist)

		basePanel.add(scroll)
		val buttonPanel = BasePanel(isVertical = false)

		buttonPanel.add(Button("Quit") {
			this.sceneController.quit()
		}.setWeight(0, 0))
		buttonPanel.add(Button("Quit2") {
			this.sceneController.quit()
		}.setWeight(0, 0))

		basePanel.add(buttonPanel)

		view.addComponent(basePanel)
		this.ui.addWindow(view)

		Thread({
			while (true) {
				Thread.sleep(2000)
				updateElements()
			}
		}, "Updating list").start()
	}

	private fun updateElements() {
		this.list.add("Test" + System.currentTimeMillis())
		this.slist.update(this.list)
	}


}