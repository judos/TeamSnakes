package ch.judos.generic.control;

public class HighPrecisionClock {

	protected int busyWaitAheadNS = 1000000;

	protected int fps;
	protected Runnable tick;
	protected boolean running;
	protected Thread thread;
	protected String name;

	public HighPrecisionClock(int fps, Runnable tick, String name) {
		this.fps = fps;
		this.tick = tick;
		this.name = name;
	}

	public void start() {
		if (!this.running) {
			this.running = true;
			this.thread = new Thread(this::run, "HighPrecisionClock (" + this.name + ")");
			this.thread.setPriority(Thread.MAX_PRIORITY);
			this.thread.start();
		}
	}

	public void stop() {
		this.running = false;
	}

	protected void run() {
		long delayBetweenFrameNS = 1000000000 / this.fps;
		long lastFrameTimeNS = 0;
		while (this.running) {
			long currentTimeNS = System.nanoTime();
			if (currentTimeNS - lastFrameTimeNS >= delayBetweenFrameNS) {
				lastFrameTimeNS = currentTimeNS;
				try {
					this.tick.run();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				long remaining =
						lastFrameTimeNS + delayBetweenFrameNS - System.nanoTime() - this.busyWaitAheadNS;
				if (remaining > 0) {
					Thread.sleep(remaining / 1000000, (int) (remaining % 1000000));
				}
			}
			catch (InterruptedException e) {
				// do nothing
			}
		}
	}

}
