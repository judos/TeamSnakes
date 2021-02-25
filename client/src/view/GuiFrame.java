package view;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import model.input.InputProvider;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @author Julian Schelker
 */
public class GuiFrame extends JFrame implements InputProvider {

	private Drawable2d drawable;
	private static final long serialVersionUID = -2940010842021558595L;

	public GuiFrame(Drawable2d drawable, GraphicsDevice deviceUsed) {
		super(deviceUsed.getDefaultConfiguration());
		this.drawable = drawable;
		this.setTitle("TeamSnakes");
		this.setSize(1920, 1080);
		this.setAlwaysOnTop(false);
		this.setIgnoreRepaint(true);
		this.setFocusTraversalKeysEnabled(false);

		this.getContentPane().requestFocusInWindow();
	}

	public void openFrame(boolean decorated) {
		this.setUndecorated(!decorated);
		this.setVisible(true);
		this.createBufferStrategy(2);
	}

	public void renderScreen() {
		try {
			BufferStrategy strategy = getBufferStrategy();

			strategy.show();
			Toolkit.getDefaultToolkit().sync();

			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			if (!this.isUndecorated()) {
				PointF t = new PointF(this.getContentPane().getLocationOnScreen())
					.subtract(this.getLocationOnScreen());
				g.translate(t.x, t.y);
				Dimension size = this.getContentPane().getSize();
				g.setClip(0, 0, size.width, size.height);
			}
			else
				g.setClip(0, 0, getWidth(), getHeight());
			this.drawable.paint(g);
			g.dispose();

		}
		catch (IllegalStateException e) {
			e.printStackTrace();
			// render should not crash
		}
	}

	@Override
	public Dimension getSize() {
		if (this.isUndecorated())
			return super.getSize();
		return this.getContentPane().getSize();
	}
}
