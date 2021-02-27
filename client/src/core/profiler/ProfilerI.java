package core.profiler;

import core.base.BaseRenderer;
import core.base.NamedComponent;
import core.input.InputHandler;

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
