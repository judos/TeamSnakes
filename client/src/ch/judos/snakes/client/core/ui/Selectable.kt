package ch.judos.snakes.client.core.ui

import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.io.InputAction
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.io.InputEvent
import java.awt.Graphics2D
import java.awt.Point
import java.util.function.Consumer

class Selectable<T>(
		var text: String,
		private val input: InputController,
		var action: Consumer<Selectable<T>>? = null
) : Label(text, false) {

	var selected: Boolean = false
	var hotkeyAction: InputAction? = null
	var data: T? = null


	fun setHotkey(hotkeyAction: InputAction): Selectable<T> {
		this.hotkeyAction = hotkeyAction
		return this
	}

	override fun handleInput(event: InputEvent) {
		if (event.isPressAction(InputAction.SELECT) && isPointInside(event.currentMousePosition)) {
			event.consume()
			toggle()
			return
		}
		if (this.hotkeyAction != null && event.isPressActionAndConsume(this.hotkeyAction)) {
			toggle()
			return
		}
	}

	override fun render(g: Graphics2D, mousePos: Point) {
		val hovered = isPointInside(this.input.getMousePosition())
		g.color = Design.selectableBackground
		if (this.selected) {
			g.color = Design.selectableSelectedBg
		} else if (hovered) {
			g.color = Design.selectableHoverBg
		}
		g.fillRect(pos.x, pos.y, size.width, size.height)

		// Render Text
		super.render(g, mousePos)

		if (this.selected) {
			g.color = Design.selectableSelectedBorder
			g.drawRect(pos.x, pos.y, size.width - 1, size.height - 1)
		} else if (isPointInside(this.input.getMousePosition())) {
			g.color = Design.selectableHoverBorder
			g.drawRect(pos.x, pos.y, size.width - 1, size.height - 1)
		}
	}

	override fun toString(): String {
		return "Selectable($text, $selected)"
	}

	protected fun toggle() {
		this.selected = !this.selected
		this.action?.accept(this)
	}


}