package ch.judos.snakes.client.core.window

import java.awt.*
import java.awt.geom.AffineTransform
import java.util.*

val stacks = HashMap<Graphics2D, GraphicsStack>()

fun Graphics2D.push() {
    val stack = this.getStack()
    stack.prepend(this.transform, this.stroke, this.color, this.font, this.clip)
    stacks[this] = stack
}

fun Graphics2D.peek() {
    val stack = this.getStack()
    if (stack.tx != null) {
        this.transform = stack.tx
        this.color = stack.color
        this.font = stack.font
        this.stroke = stack.stroke
        this.clip = stack.clip
    }
}

fun Graphics2D.pop() {
    val stack = this.getStack()
    if (stack.tx != null) {
        this.peek()
        stacks[this] = stack.tail!!
    }
}

fun Graphics2D.getStack(): GraphicsStack {
    return stacks[this] ?: GraphicsStack()
}

fun Graphics2D.setStack(stack: GraphicsStack) {
    stacks[this] = stack
    peek()
}

fun Graphics2D.drawCenterText(string: String, x: Int, y: Int) {
    val width: Int = this.fontMetrics.stringWidth(string)
    this.drawString(string, x - width / 2, y)
}

fun Graphics2D.setRenderHints() {
    val hints = HashMap<RenderingHints.Key, Any>()
    hints[RenderingHints.KEY_TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    hints[RenderingHints.KEY_INTERPOLATION] = RenderingHints.VALUE_INTERPOLATION_BILINEAR
    this.setRenderingHints(hints)
}

class GraphicsStack() {
    var tx: AffineTransform? = null
    var stroke: Stroke? = null
    var color: Color? = null
    var font: Font? = null
    var clip: Shape? = null
    var tail: GraphicsStack? = null

    constructor(tx: AffineTransform?, stroke: Stroke?, color: Color?, font: Font?, clip: Shape?,
                tail: GraphicsStack?) : this() {
        this.tx = tx
        this.stroke = stroke
        this.color = color
        this.font = font
        this.clip = clip
        this.tail = tail
    }

    fun prepend(tx: AffineTransform?, stroke: Stroke?, color: Color?, font: Font?, clip: Shape?): GraphicsStack {
        return GraphicsStack(tx, stroke, color, font, clip, this)
    }
}


