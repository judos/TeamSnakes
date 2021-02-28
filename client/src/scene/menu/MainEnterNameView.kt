package scene.menu

import core.base.Design
import core.input.InputController
import core.ui.BasePanel
import core.ui.Button
import core.ui.InputField
import core.ui.WindowComponent

class MainEnterNameView(
		private val input: InputController
) : WindowComponent(
		Design.titleFont, input
) {

	private var inputField: InputField

	init {
		this.title = "Enter your player name"
		this.inputField = InputField(30, input)
		this.inputField.focus()
		addComponent(this.inputField)

		val panel = BasePanel(isVertical = false)
		panel.add(Button("Ok", input, this::saveName))
		panel.add(Button("Exit", input, this::dispose))
		addComponent(panel)
	}

	private fun saveName() {
		this.dispose(this.inputField.text)
	}

}