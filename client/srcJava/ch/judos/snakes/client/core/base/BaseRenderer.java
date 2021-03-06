package ch.judos.snakes.client.core.base;

import java.awt.*;

@FunctionalInterface
public interface BaseRenderer extends NamedComponent {

	void render(Graphics graphics);

}
