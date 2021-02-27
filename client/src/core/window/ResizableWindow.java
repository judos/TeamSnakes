package core.window;

import core.input.InputController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;

public class ResizableWindow implements GameWindow {

	private InputController input;
	private Graphics2D graphics;

	private JFrame window;
	private Canvas canvas;
	private BufferStrategy buffer;
	private boolean resized = false;

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
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		window.setVisible(true);

		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();

		canvas.setFocusable(false);

		canvas.addMouseListener(input.getMouse());
		canvas.addMouseMotionListener(input.getMouse());
		canvas.addMouseWheelListener(input.getMouse());
		window.addKeyListener(input.getKeyboard());
		window.addComponentListener(compListener);
		window.setFocusTraversalKeysEnabled(false);
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
