package ch.judos.snakes.client.core.window;

import ch.judos.snakes.client.core.io.InputController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class ResizableWindow implements GameWindow {

	private InputController input;
	private Graphics2D graphics;

	private JFrame window;
	private Canvas canvas;
	private BufferStrategy buffer;
	private boolean resized = false;

	public Runnable onClosed;

	private ComponentListener compListener = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			resized = true;
		}
	};

	public ResizableWindow(String title, int x, int y, InputController input) {
		this.input = input;
		window = new JFrame(title);
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(x, y));
		window.add(canvas);
		init();
	}

	@Override
	public Dimension getScreenSize() {
		return canvas.getSize();
	}

	@Override
	public InputController getInput() {
		return input;
	}
	@Override
	public Graphics2D getGraphics() {
		return graphics;
	}
	
	private void init() {
		window.setIgnoreRepaint(true);
		canvas.setIgnoreRepaint(true);
		window.pack();
		this.centerOnScreen();
		window.setVisible(true);

		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();

		canvas.setFocusable(false);

		canvas.addMouseListener(input.getMouse());
		canvas.addMouseMotionListener(input.getMouse());
		canvas.addMouseWheelListener(input.getMouse());
		window.addKeyListener(input.getKeyboard());
		window.addComponentListener(compListener);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (onClosed != null) {
					onClosed.run();
				}
			}
		});
		window.setFocusTraversalKeysEnabled(false);
	}

	private void centerOnScreen() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = env.getScreenDevices();
		int screenNr = Math.min(0, screens.length);
		Rectangle bounds = screens[screenNr].getDefaultConfiguration().getBounds();
		int windowPosX = ((bounds.width - window.getWidth()) / 2) + bounds.x;
		int windowPosY = ((bounds.height - window.getHeight()) / 2) + bounds.y;
		window.setLocation(windowPosX, windowPosY);
	}

	@Override
	public void discard() {
		buffer.dispose();

		canvas.removeMouseListener(input.getMouse());
		canvas.removeMouseMotionListener(input.getMouse());
		canvas.removeMouseWheelListener(input.getMouse());
		window.removeKeyListener(input.getKeyboard());
		window.removeComponentListener(compListener);

		window.setVisible(false);
	}

	@Override
	public void flipFrame() {
		if (graphics != null) graphics.dispose();
		if (buffer.contentsLost() || buffer.contentsRestored()) {
			buffer = canvas.getBufferStrategy();
		}
		buffer.show();
		graphics = (Graphics2D) buffer.getDrawGraphics();
		graphics.setBackground(GameWindow.BG_COLOR);
		graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@Override
	public boolean resized() {
		boolean res = resized;
		resized = false;
		return res;
	}

}
