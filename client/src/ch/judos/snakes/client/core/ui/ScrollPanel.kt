package ch.judos.snakes.client.core.ui

import ch.judos.snakes.client.core.io.InputAction
import ch.judos.snakes.client.core.io.InputEvent
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionH
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionV
import org.apache.logging.log4j.LogManager
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

open class ScrollPanel(
		var preferedSize: Dimension
) : BasePanel(PositionH.LEFT, PositionV.TOP, true, true) {

	protected val logger = LogManager.getLogger(javaClass)

	var scrollPos = 0.0
	var targetScrollPos = 0


	override fun render(g: Graphics2D, mousePos: Point) {
		updateScrollPos()
		this.renderPanel(g)
		val clip = g.clip
		g.clipRect(pos.x, pos.y, size.width, size.height)
		g.translate(0, -scrollPos.toInt())
		mousePos.translate(0, scrollPos.toInt())
		for (b in components) {
			if (b.pos.y - scrollPos >= this.pos.y + this.size.height) {
				continue
			}
			if (b.pos.y + b.size.height - scrollPos < this.pos.y) {
				continue
			}
			b.render(g, mousePos)
		}
		mousePos.translate(0, -scrollPos.toInt())
		g.translate(0, scrollPos.toInt())
		g.clip = clip
	}

	private fun updateScrollPos() {
		val diff = targetScrollPos - scrollPos
		if (diff != 0.0) {
			val change = sign(diff) * min(min(max(2.0, 0.2 * abs(diff)), 20.0), abs(diff))
			scrollPos += change
		}
	}

	override fun layout(x: Int, y: Int, w: Int, h: Int) {
		super.layout(x, y, w, h)
	}

	override fun handleInput(event: InputEvent) {
		if (!isPointInside(event.currentMousePosition))
			return
		if (event.isActionAndConsume(InputAction.MOUSE_SCROLL)) {
			targetScrollPos += 32 * event.changeValue.toInt()
			if (targetScrollPos < 0) targetScrollPos = 0
			val contentPreferedSize = super.getPreferedDimension()
			val upper = max(0, contentPreferedSize.height - this.size.height)
			if (targetScrollPos > upper) {
				targetScrollPos = upper
			}
			return
		}

		event.currentMousePosition.translate(0, scrollPos.toInt())
		super.handleInput(event)
		event.currentMousePosition.translate(0, -scrollPos.toInt())
	}


	override fun getStretchWeightY(): Int {
		// components should not stretch in height
		return 0
	}

	override fun getPreferedDimension(): Dimension {
		return preferedSize
	}

}