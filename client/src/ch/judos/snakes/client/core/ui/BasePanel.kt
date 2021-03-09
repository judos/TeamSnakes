package ch.judos.snakes.client.core.ui

import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.io.InputEvent
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionH
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionV
import ch.judos.snakes.common.extensions.maxSum
import ch.judos.snakes.common.extensions.sumMax
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point
import java.util.stream.Collectors

open class BasePanel @JvmOverloads constructor(
		private val positionH: PositionH = PositionH.CENTER,
		private val positionV: PositionV = PositionV.CENTER,
		var isVertical: Boolean = true,
		var drawBorder: Boolean = false
) : BaseComponent() {

	protected val components = mutableListOf<Component>()

	fun add(vararg components: Component): BasePanel {
		this.components.addAll(components)
		return this
	}

	protected fun renderPanel(g: Graphics2D) {
		g.color = Design.panelBackground
		g.fillRect(pos.x, pos.y, size.width, size.height)
		if (this.drawBorder) {
			g.color = Design.grayBorder
			g.drawRect(pos.x, pos.y, size.width - 1, size.height - 1)
		}
	}

	override fun render(g: Graphics2D, mousePos: Point) {
		this.renderPanel(g)
		for (b in components) {
			b.render(g, mousePos)
		}
	}

	override fun layout(x: Int, y: Int, w: Int, h: Int) {
		super.layout(x, y, w, h)
		val prefered = this.preferedDimension
		var addedPerStretch = 0
		if (isVertical) {
			var currentPos = y
			val stretch = getStretchWeightY()
			val remaining = h - prefered.height
			if (stretch > 0) addedPerStretch = remaining / stretch
			for (c in components) {
				val layout = LayoutPositioning(positionH, positionV, c.preferedDimension)
						.setStretch(c.stretchWeightX > 0, c.stretchWeightY > 0)
				val componentSize = c.preferedDimension
				val add = c.stretchWeightY * addedPerStretch

				val l = layout.getInBounds(x, currentPos, w, componentSize.height + add)
				c.layout(l[0], l[1], l[2], l[3])
				currentPos += l[3];
			}
		} else {
			var currentPos = x
			val stretch = getStretchWeightX()
			val remaining = w - prefered.width
			if (stretch > 0) addedPerStretch = remaining / stretch
			for (c in components) {
				val layout = LayoutPositioning(positionH, positionV, c.preferedDimension)
						.setStretch(c.stretchWeightX > 0, c.stretchWeightY > 0)
				val componentSize = c.preferedDimension
				val add = c.stretchWeightX * addedPerStretch

				val l = layout.getInBounds(currentPos, y, componentSize.width + add, h)
				c.layout(l[0], l[1], l[2], l[3])
				currentPos += l[2];
			}
		}
	}

	override fun getStretchWeightX(): Int {
		return components.stream().mapToInt { c: Component -> c.stretchWeightX }.sum()
	}

	override fun getStretchWeightY(): Int {
		return components.stream().mapToInt { c: Component -> c.stretchWeightY }.sum()
	}

	override fun getPreferedDimension(): Dimension {
		return if (isVertical)
			this.components.map { it.preferedDimension }.maxSum()
		else
			this.components.map { it.preferedDimension }.sumMax()
	}

	override fun handleInput(event: InputEvent) {
		for (b in components) {
			b.handleInput(event)
			if (event.isConsumed) return
		}
	}

	override fun toString(): String {
		return "BasePanel(" + components.stream().map { c: Component -> c.toString() }
				.collect(Collectors.joining(", ")) + ")"
	}

}