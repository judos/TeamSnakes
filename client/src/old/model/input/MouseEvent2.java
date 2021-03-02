package old.model.input;

import java.awt.event.MouseEvent;

import ch.judos.generic.data.geometry.PointI;

/**
 * @author Julian Schelker
 */
public class MouseEvent2 {

	protected InputType type;
	protected PointI screenPosition;
	protected int button;
	protected PointI mapPosition;

	public MouseEvent2(InputType type, MouseEvent event, PointI onMap) {
		this(type, new PointI(event.getPoint()), onMap, event.getButton());
	}

	protected MouseEvent2(InputType type, PointI screen, PointI onMap, int button) {
		this.type = type;
		this.screenPosition = screen;
		this.mapPosition = onMap;
		this.button = button;
	}

	public PointI getMapPosition() {
		return this.mapPosition;
	}

	public InputType getType() {
		return this.type;
	}

	public PointI getScreenPosition() {
		return this.screenPosition;
	}

	public int getButton() {
		return this.button;
	}

	public MouseEvent2 deepCopy() {
		return new MouseEvent2(this.type, this.screenPosition.deepCopy(), this.mapPosition
			.deepCopy(), this.button);
	}

}
