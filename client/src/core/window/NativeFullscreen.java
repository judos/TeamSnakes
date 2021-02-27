package core.window;

import core.input.InputController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class NativeFullscreen implements GameWindow {

	private InputController input;
	private Graphics2D graphics;
	private Dimension screenSize;

	private GraphicsDevice screen;
	private JFrame window;
	private Canvas canvas;
	private BufferStrategy buffer;

	public NativeFullscreen(InputController input) {
		this.input = input;
		screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		window = new JFrame();
		canvas = new Canvas();
		Rectangle bounds = screen.getDefaultConfiguration().getBounds();
		this.screenSize = bounds.getSize();
		window.add(canvas);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
	}

	@Override
	public InputController getInput() {
		return input;
	}
	@Override
	public Graphics2D getGraphics() {
		return graphics;
	}
	@Override
	public Dimension getScreenSize() {
		return screenSize;
	}

	private void init() {

		window.setVisible(true);
		window.setResizable(false);

		screen.setFullScreenWindow(window);

		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();

		window.setVisible(false);
		window.setVisible(true);
		canvas.setFocusable(false);

		canvas.addMouseListener(input.getMouse());
		canvas.addMouseMotionListener(input.getMouse());
		canvas.addMouseWheelListener(input.getMouse());
		window.addKeyListener(input.getKeyboard());
		window.setFocusTraversalKeysEnabled(false);
	}

	@Override
	public void discard() {
		buffer.dispose();
		screen.setFullScreenWindow(null);

		window.setVisible(false);

		canvas.removeMouseListener(input.getMouse());
		canvas.removeMouseMotionListener(input.getMouse());
		canvas.removeMouseWheelListener(input.getMouse());
		window.removeKeyListener(input.getKeyboard());
	}

	@Override
	public void flipFrame() {
		if (graphics != null) graphics.dispose();
		buffer.show();
		graphics = (Graphics2D) buffer.getDrawGraphics();
		graphics.setBackground(GameWindow.BG_COLOR);
		graphics.clearRect(0, 0, this.screenSize.width, this.screenSize.height);
	}


}
