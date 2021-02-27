package core.profiler;

import core.base.NamedComponent;
import core.input.InputAction;
import core.input.InputEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map.Entry;

public final class Profiler implements ProfilerI {

	/**
	 * how many frames to analyze before outputting new string data
	 */
	private final int sampleCount;

	/**
	 * time used to calculate
	 */
	private long frameActiveDuration;

	/**
	 * duration between starts of frames
	 */
	private long frameDuration;

	/**
	 * last time of a frame start
	 */
	private long currentFrameStart;

	/**
	 * count the frames till sampleCount where string output is updated and data cleared
	 */
	private int frame;

	/**
	 * time used by components
	 */
	private HashMap<NamedComponent, Long> activeTimeOfComponents;

	private String digest = "no data yet";

	private boolean show;

	public Profiler(int sampleCount) {
		this.sampleCount = sampleCount;
		this.activeTimeOfComponents = new HashMap<>();
		this.currentFrameStart = System.currentTimeMillis();
	}

	private void reset() {
		frameActiveDuration = 0;
		frameDuration = 0;
		this.activeTimeOfComponents.clear();
	}

	public void startFrame() {
		long newFrameStart = System.currentTimeMillis();
		frameDuration += newFrameStart - currentFrameStart;
		currentFrameStart = newFrameStart;
	}

	public void startSample(NamedComponent component) {
		if (!this.activeTimeOfComponents.containsKey(component)) {
			this.activeTimeOfComponents.put(component, -System.currentTimeMillis());
		} else {
			this.activeTimeOfComponents.put(component, this.activeTimeOfComponents.get(component) - System.currentTimeMillis());
		}
	}

	public void endSample(NamedComponent component) {
		this.activeTimeOfComponents.put(component, this.activeTimeOfComponents.get(component) + System.currentTimeMillis());
	}

	public void endFrame() {
		frameActiveDuration += System.currentTimeMillis() - currentFrameStart;
		frame++;
		if (frame == sampleCount) {
			digest();
			reset();
			frame = 0;
		}
	}

	private void digest() {
		StringBuilder sb = new StringBuilder();
		sb.append(1000 / (frameDuration / frame) + " FPS\n");
		sb.append((frameActiveDuration / frame) + "ms (" + ((100 * frameActiveDuration) / frameDuration) + "% load)\n");
		sb.append("\n");

		long sum = 0;
		for (Entry<NamedComponent, Long> c : this.activeTimeOfComponents.entrySet()) {
			sb.append(" - " + c.getKey().getName() + ": " + (c.getValue() / frame) + "ms\n");
			sum += c.getValue();
		}
		sb.append("Components: " + (sum / frame) + "ms\n");
		digest = sb.toString();
	}

	@Override
	public String[] getProfilerOutput() {
		return this.digest.split("\n");
	}

	@Override
	public void render(Graphics graphics) {
		if (!this.show) return;
		FontMetrics metrics = graphics.getFontMetrics();
		int sh = metrics.getHeight();
		String[] digest = this.getProfilerOutput();

		graphics.setColor(new Color(255, 255, 255, 200));
		graphics.fillRect(0, 0, 200, sh * (digest.length + 2));
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < digest.length; i++) {
			graphics.drawString(digest[i], 5, (i + 1) * sh);
		}
	}

	@Override
	public void handleInput(InputEvent event) {
		if (event.isPressAction(InputAction.PROFILER)) {
			event.consume();
			this.show = !this.show;
		}
	}

}
