package ch.judos.snakes.client.core.ui

import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.io.InputEvent
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionH
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionV
import java.awt.Dimension
import java.awt.Graphics
import java.util.stream.Collectors

class BasePanel @JvmOverloads constructor(
		private val positionH: PositionH = PositionH.CENTER,
		private val positionV: PositionV = PositionV.CENTER,
		var isVertical: Boolean = true
) : BaseComponent() {

	private val components = mutableListOf<Component>()

	fun add(vararg components: Component): BasePanel {
		this.components.addAll(components)
		return this
	}

	override fun render(g: Graphics) {
		g.color = Design.panelBackground
		g.fillRect(pos.x, pos.y, size.width, size.height)
		g.color = Design.grayBorder
		g.drawRect(pos.x, pos.y, size.width, size.height)
		for (b in components) {
			b.render(g)
		}
	}

	override fun layout(x: Int, y: Int, w: Int, h: Int) {
		val prefered = this.preferedDimension
		val stretchX = getStretchWeightX()
		val stretchY = getStretchWeightY()
		size = Dimension(w, h)
		if (stretchX == 0) size.width = prefered.width
		if (stretchY == 0) size.height = prefered.height
		val layout = LayoutPositioning(positionH, positionV, size)
		pos = layout.getPixelPositionBasedOnEnums(x, y, w, h)
		var currentOffset = 0
		var addedPerStretch = 0
		if (isVertical) {
			val remaining = h - prefered.height
			if (stretchY > 0) addedPerStretch = remaining / stretchY
			for (c in components) {
				val componentSize = c.preferedDimension
				val add = c.stretchWeightY * addedPerStretch
				c.layout(pos.x, pos.y + currentOffset, size.width, componentSize.height + add)
				currentOffset += componentSize.height + add
			}
		} else {
			val remaining = w - prefered.width
			if (stretchX > 0) addedPerStretch = remaining / stretchX
			for (c in components) {
				val componentSize = c.preferedDimension
				val add = c.stretchWeightX * addedPerStretch
				c.layout(pos.x + currentOffset, pos.y, componentSize.width + add, size.height)
				currentOffset += componentSize.width + add
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
		var width = 0
		var height = 0
		for (b in components) {
			val d = b.preferedDimension
			if (isVertical) {
				width = Math.max(width, d.width)
				height += d.height
			} else {
				width += d.width
				height = Math.max(height, d.height)
			}
		}
		return Dimension(width, height)
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