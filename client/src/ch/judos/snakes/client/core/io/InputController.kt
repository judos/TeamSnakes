package ch.judos.snakes.client.core.io

import ch.judos.snakes.client.core.base.NamedComponent
import ch.judos.snakes.client.core.io.InputEvent.InputEventType
import org.apache.logging.log4j.LogManager
import java.awt.Point
import java.awt.event.*
import java.util.*

class InputController : NamedComponent {
	protected val logger = LogManager.getLogger(javaClass)

	// EVENT PROCESSING
	private var queuedInputEvents: MutableList<InputEvent> = ArrayList()
	private var hoveringEnabled: Boolean = true

	// CACHE
	private val holdKeyCodes: HashSet<Int> = HashSet()
	private var currentMousePosition: Point = Point(-1, -1)
	var currentlyFocused: InputHandler? = null
		private set

	fun popAllEvents(): List<InputEvent> {
		val result: List<InputEvent> = queuedInputEvents
		queuedInputEvents = ArrayList()
		return result
	}

	// ADAPTERS
	val keyboard: KeyAdapter = object : KeyAdapter() {
		override fun keyPressed(e: KeyEvent) {
			var keycode = e.keyCode
			if (keycode == 0) keycode = e.extendedKeyCode
			var c: Char? = e.keyChar
			if (!Character.isDefined(c!!)) c = null
			queuedInputEvents.add(InputEvent(keycode, InputEventType.PRESS, 0.0, HashSet(holdKeyCodes), currentMousePosition, c))
			holdKeyCodes.add(e.keyCode)
		}

		override fun keyReleased(e: KeyEvent) {
			var keycode = e.keyCode
			if (keycode == 0) keycode = e.extendedKeyCode
			holdKeyCodes.remove(e.keyCode)
			queuedInputEvents.add(InputEvent(keycode, InputEventType.RELEASE, 0.0, HashSet(holdKeyCodes), currentMousePosition))
		}
	}
	val mouse: MouseAdapter = object : MouseAdapter() {
		override fun mouseDragged(e: MouseEvent) {
			mouseMoved(e)
		}

		override fun mouseMoved(e: MouseEvent) {
			val p = e.point ?: return
			currentMousePosition = p
		}

		override fun mousePressed(e: MouseEvent) {
			val code = btnToCode(e.button)
			queuedInputEvents.add(InputEvent(code, InputEventType.PRESS, 0.0, holdKeyCodes, currentMousePosition))
			holdKeyCodes.add(code)
		}

		override fun mouseReleased(e: MouseEvent) {
			val code = btnToCode(e.button)
			holdKeyCodes.remove(code)
			queuedInputEvents.add(InputEvent(code, InputEventType.RELEASE, 0.0, holdKeyCodes, currentMousePosition))
		}

		override fun mouseWheelMoved(e: MouseWheelEvent) {
			val code = MOUSE_SCROLL
			queuedInputEvents.add(InputEvent(code, InputEventType.CHANGE, e.preciseWheelRotation, holdKeyCodes, currentMousePosition))
		}

		private fun btnToCode(button: Int): Int {
			if (button == MouseEvent.BUTTON1) return MOUSE_LEFT
			if (button == MouseEvent.BUTTON2) return MOUSE_CENTER
			if (button == MouseEvent.BUTTON3) return MOUSE_RIGHT
			logger.warn("Unknown mouse button: $button")
			return MOUSE_CENTER
		}
	}

	/**
	 * @return mouse coordinate, DO NOT MODIFY THIS OBJECT!
	 */
	val mousePosition: Point
		get() = if (!hoveringEnabled) {
			Point(-2, -2)
		} else currentMousePosition

	fun setFocus(handler: InputHandler?) {
		currentlyFocused = handler
	}

	fun unfocus(handler: InputHandler) {
		if (currentlyFocused === handler) currentlyFocused = null
	}

	fun handleInput(event: InputEvent) {
		if (currentlyFocused != null) {
			if (event.isPressActionAndConsume(InputAction.DESELECT)) {
				currentlyFocused!!.unfocus()
				currentlyFocused = null
			} else {
				currentlyFocused!!.handleInput(event)
			}
		}
	}

	fun setHoverEnabled(b: Boolean) {
		hoveringEnabled = b
	}

	companion object {
		// CONSTANTS
		const val MOUSE_LEFT = 0x00010000
		const val MOUSE_CENTER = 0x00020000
		const val MOUSE_RIGHT = 0x00030000
		const val MOUSE_SCROLL = 0x00040000
	}
}