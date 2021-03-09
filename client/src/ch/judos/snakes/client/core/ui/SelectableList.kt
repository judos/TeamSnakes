package ch.judos.snakes.client.core.ui

import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.io.InputEvent
import ch.judos.snakes.common.extensions.maxSum
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point

class SelectableList<T>(
		list: List<T>,
		private var inputController: InputController
) : BaseComponent() {

	private val components = mutableListOf<Selectable<T>>()

	private var selectedE: Selectable<T>? = null
		set(value) {
			field?.selected = false
			field = value
			if (value?.selected == true) {
				field?.selected = true
			}
		}

	var selected: T?
		set(value) {
			selectedE = this.components.firstOrNull { it.data == value }
		}
		get() = selectedE?.data

	init {
		addComponents(list)
	}

	private fun addComponents(list: List<T>) {
		for (t in list) {
			val s = Selectable<T>(t.toString(), inputController) {
				selectedE = it
			}
			s.data = t
			components.add(s)
		}
	}

	fun update(list: List<T>) {
		val selected = this.selected
		this.components.clear()
		addComponents(list)
		this.selected = selected

		var y2 = this.pos.y
		for (c in components) {
			c.layout(this.pos.x, y2, this.size.width, c.preferedDimension.height)
			y2 += c.preferedDimension.height
		}
		this.size.height = y2
	}

	override fun layout(x: Int, y: Int, w: Int, h: Int) {
		super.layout(x, y, w, h)
		var y2 = y
		for (c in components) {
			c.layout(x, y2, w, c.preferedDimension.height)
			y2 += c.preferedDimension.height
		}
	}

	override fun render(g: Graphics2D, mousePos: Point) {
		for (b in components) {
			b.render(g, mousePos)
		}
	}

	override fun handleInput(event: InputEvent) {
		for (b in components) {
			b.handleInput(event)
			if (event.isConsumed) return
		}
	}

	override fun getPreferedDimension(): Dimension {
		return this.components.map { it.preferedDimension }.maxSum()
	}

}