package core.ui;

import core.base.Design;
import core.input.InputEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import static core.ui.LayoutPositioning.*;

public class BasePanel extends BaseComponent {

	private ArrayList<Component> components;
	private PositionH positionH;
	private PositionV positionV;
	public boolean isVertical;

	/**
	 * by default inner components are stacked vertically
	 */
	public BasePanel() {
		this(PositionH.CENTER, PositionV.CENTER, true);
	}

	public BasePanel(PositionH positionH, PositionV positionV, boolean isVertical) {
		this.positionH = positionH;
		this.positionV = positionV;
		this.isVertical = isVertical;
		this.components = new ArrayList<>();
	}


	public BasePanel add(Component... components) {
		Collections.addAll(this.components, components);
		return this;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Design.panelBackground);
		g.fillRect(this.pos.x, this.pos.y, this.size.width, this.size.height);
		g.setColor(Design.grayBorder);
		g.drawRect(this.pos.x, this.pos.y, this.size.width, this.size.height);

		for (Component b : this.components) {
			b.render(g);
		}
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		Dimension prefered = this.getPreferedDimension();
		int stretchX = this.getStretchWeightX();
		int stretchY = this.getStretchWeightY();
		this.size = new Dimension(w, h);
		if (stretchX == 0)
			this.size.width = prefered.width;
		if (stretchY == 0)
			this.size.height = prefered.height;
		LayoutPositioning layout = new LayoutPositioning(this.positionH, this.positionV, this.size);
		this.pos = layout.getPixelPositionBasedOnEnums(x, y, w, h);

		int currentOffset = 0;
		int addedPerStretch = 0;
		if (this.isVertical) {
			int remaining = h - prefered.height;
			if (stretchY > 0)
				addedPerStretch = remaining / stretchY;
			for (Component c : this.components) {
				Dimension componentSize = c.getPreferedDimension();
				int add = c.getStretchWeightY() * addedPerStretch;
				c.layout(this.pos.x, this.pos.y + currentOffset, this.size.width, componentSize.height + add);
				currentOffset += componentSize.height + add;
			}
		} else {
			int remaining = w - prefered.width;
			if (stretchX > 0)
				addedPerStretch = remaining / stretchX;
			for (Component c : this.components) {
				Dimension componentSize = c.getPreferedDimension();
				int add = c.getStretchWeightX() * addedPerStretch;
				c.layout(this.pos.x + currentOffset, this.pos.y, componentSize.width + add, this.size.height);
				currentOffset += componentSize.width + add;
			}
		}
	}

	@Override
	public int getStretchWeightX() {
		return this.components.stream().mapToInt(c -> c.getStretchWeightX()).sum();
	}

	@Override
	public int getStretchWeightY() {
		return this.components.stream().mapToInt(c -> c.getStretchWeightY()).sum();
	}

	@Override
	public Dimension getPreferedDimension() {
		int width = 0;
		int height = 0;
		for (Component b : this.components) {
			Dimension d = b.getPreferedDimension();
			if (this.isVertical) {
				width = Math.max(width, d.width);
				height += d.height;
			} else {
				width += d.width;
				height = Math.max(height, d.height);
			}
		}
		return new Dimension(width, height);
	}

	@Override
	public void handleInput(InputEvent event) {
		for (Component b : this.components) {
			b.handleInput(event);
			if (event.isConsumed)
				return;
		}
	}

	@Override
	public String toString() {
		return "BasePanel(" + this.components.stream().map(c -> c.toString()).collect(Collectors.joining(", ")) + ")";
	}

}
