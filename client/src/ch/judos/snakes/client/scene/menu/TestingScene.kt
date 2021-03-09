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

	init {
		val view = WindowComponent(Design.titleFont)
		view.title = "Testing Scene"

		val list = SelectableList(listOf("Test", "Test2", "Test3", "Test4", "Test5"), inputController)
				.setWeight(1,0)
		val scroll = ScrollPanel(Dimension(100, 100))
		scroll.add(list)

		view.addComponent(Spacer(1,150))
		view.addComponent(scroll)
		view.addComponent(Spacer(1,150))
		val buttonPanel = BasePanel(isVertical = false)

		buttonPanel.add(Button("Quit") {
			this.sceneController.quit()
		}.setWeight(0,0))
		buttonPanel.add(Button("Quit2") {
			this.sceneController.quit()
		}.setWeight(0,0))

		view.addComponent(buttonPanel)

		this.ui.addWindow(view)
	}


}