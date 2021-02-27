package core.base;

import java.awt.*;

@FunctionalInterface
public interface BaseRenderer extends NamedComponent {

	void render(Graphics graphics);

}
