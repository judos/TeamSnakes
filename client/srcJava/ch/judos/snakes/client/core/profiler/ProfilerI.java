package ch.judos.snakes.client.core.profiler;

import ch.judos.snakes.client.core.io.InputHandler;
import ch.judos.snakes.client.core.base.BaseRenderer;
import ch.judos.snakes.client.core.base.NamedComponent;

public interface ProfilerI extends BaseRenderer, InputHandler {

	public void startSample(NamedComponent controller);

	public void endSample(NamedComponent controller);

	public void startFrame();

	public void endFrame();

	/**
	 * @return return the data in string representation of the last sample period
	 */
	public String[] getProfilerOutput();
}
