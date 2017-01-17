package view;

import java.awt.*;
import java.awt.event.*;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import model.input.InputProvider;
import ch.judos.generic.control.Log;
import ch.judos.generic.graphics.Drawable2d;
import ch.judos.generic.graphics.fullscreen.FullScreen;

/**
 * @author Julian Schelker
 */
public class Gui implements Drawable2d {

	GuiFrame frame;
	private Timer timer;
	private Drawable2d drawable;
	private GraphicsDevice deviceUsed;
	Runnable update;
	private boolean isRunning;
	private Thread renderThread;

	public Gui(Optional<Runnable> onQuit) {
		GraphicsDevice[] dev = FullScreen.getDevices();
		this.deviceUsed = dev[0];
		this.update = null;
		this.isRunning = false;

		this.frame = new GuiFrame(this, this.deviceUsed);
		if (onQuit.isPresent()) {
			this.frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					onQuit.get().run();
				}
			});
		}
	}

	public void setUpdate(Runnable r) {
		this.update = r;
	}

	public void startViewTimer(int fps) {
		this.frame.openFrame(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Gui.this.frame.renderScreen();
			}
		};
		this.timer = new Timer("Render thread", false);
		int delay = (int) (1000. / fps);
		this.timer.scheduleAtFixedRate(task, delay, delay);
	}

	public void startFullScreen(int fps) {
		this.frame.setUndecorated(true);
		this.deviceUsed.setFullScreenWindow(this.frame);
		// workaround: sometimes after loosing focus, no input works anymore on
		// the frame
		// note: still the case for double-screen setups on windows
		this.frame.setVisible(false);
		this.frame.setVisible(true);
		// end of workaround

		this.frame.createBufferStrategy(2);
		startViewWithoutOpeningFrame(fps);
	}
	public void startView(int fps) {
		this.frame.openFrame(true);
		startViewWithoutOpeningFrame(fps);
	}

	public void startViewUndecorated(int fps) {
		this.frame.openFrame(false);
		startViewWithoutOpeningFrame(fps);
	}

	private void startViewWithoutOpeningFrame(int fps) {
		this.isRunning = true;
		this.renderThread = new Thread("Render thread") {
			@Override
			public void run() {
				long delay = 1000000000 / fps;
				long lastFrame = System.nanoTime();
				while (Gui.this.isRunning) {
					long ns = System.nanoTime();
					if (ns - lastFrame >= delay) {
						lastFrame = ns;
						try {
							if (Gui.this.update != null)
								Gui.this.update.run();
							Gui.this.frame.renderScreen();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						long remaining = lastFrame + delay - System.nanoTime() - 1000000;
						if (remaining > 0)
							Thread.sleep(remaining / 1000000, (int) (remaining % 1000000));
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				renderingDidEnd();
			}
		};
		this.renderThread.setPriority(Thread.MAX_PRIORITY);
		this.renderThread.start();
	}

	public void startViewWhileTrue() {
		this.frame.openFrame(true);
		this.isRunning = true;
		this.renderThread = new Thread("Render thread") {
			@Override
			public void run() {
				while (isRunning) {
					Gui.this.frame.renderScreen();
				}
				renderingDidEnd();
			}
		};
		this.renderThread.start();
	}

	public void setDrawable(Drawable2d m) {
		this.drawable = m;
	}

	public Dimension getSize() {
		return this.frame.getSize();
	}

	public InputProvider getInputProvider() {
		return new InputProvider() {
			@Override
			public void addMouseListener(MouseListener m) {
				Gui.this.frame.addMouseListener(m);
			}

			@Override
			public void addKeyListener(KeyListener k) {
				Gui.this.frame.addKeyListener(k);
			}

			@Override
			public void addMouseWheelListener(MouseWheelListener m) {
				Gui.this.frame.addMouseWheelListener(m);
			}

		};
	}

	@Override
	public void paint(Graphics2D g) {
		Rectangle size = g.getClipBounds();
		g.setBackground(Color.black);
		g.clearRect(0, 0, size.width, size.height);
		if (this.drawable != null)
			this.drawable.paint(g);
	}

	public Component getComponent() {
		return this.frame.getContentPane();
	}

	public void quit() {
		Log.info("quit");
		this.isRunning = false;
		if (this.timer != null) {
			this.timer.cancel();
		}
	}

	private void renderingDidEnd() {
		Log.info("render thread finished");
		Log.info("disposing frame");
		this.frame.setVisible(false);
		this.frame.dispose();
	}

}
