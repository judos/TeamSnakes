package old.model.game;

import ch.judos.generic.data.geometry.PointI;
import old.model.game.space.Locatable;

public class EatablePoint implements Locatable {
	private int size;
	private PointI position;

	public EatablePoint(PointI position, int size) {
		this.position = position;
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}

	@Override
	public PointI getLocation() {
		return this.position;
	}

	@Override
	public String toString() {
		return "EP " + this.position.x + "," + this.position.y + " S: " + this.size + ")";
	}
}
