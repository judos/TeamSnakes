package ch.judos.snakes.client.core.profiler;

import ch.judos.snakes.client.core.base.Design;
import ch.judos.snakes.client.core.io.InputAction;
import ch.judos.snakes.client.core.io.InputEvent;
import ch.judos.snakes.client.core.base.NamedComponent;

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
		sb.append(1000 / (frameDuration / frame)).append(" FPS\n");
		sb.append(frameActiveDuration / frame)
				.append("ms (")
				.append((100 * frameActiveDuration) / frameDuration)
				.append("% load)\n\n");

		long sum = 0;
		for (Entry<NamedComponent, Long> c : this.activeTimeOfComponents.entrySet()) {
			sb.append(" - ").append(c.getKey().getName()).append(": ").append(c.getValue() / frame).append("ms\n");
			sum += c.getValue();
		}
		sb.append("Components: ").append(sum / frame).append("ms\n");
		digest = sb.toString();
	}

	@Override
	public String[] getProfilerOutput() {
		return this.digest.split("\n");
	}

	@Override
	public void render(Graphics2D g, Point mousePos) {
		if (!this.show) return;
		g.setFont(Design.textFont);
		FontMetrics metrics = g.getFontMetrics();
		int sh = metrics.getHeight();
		String[] digest = this.getProfilerOutput();

		g.setColor(new Color(255, 255, 255, 200));
		g.fillRect(0, 0, 200, sh * (digest.length + 2));
		g.setColor(Color.BLACK);
		for (int i = 0; i < digest.length; i++) {
			g.drawString(digest[i], 5, (i + 1) * sh);
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
