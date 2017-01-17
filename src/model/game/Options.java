package model.game;

public class Options {

	private boolean debuggingEnabled;

	public boolean isDebuggingEnabled() {
		return this.debuggingEnabled;
	}

	public void toggleDebugOptions() {
		this.debuggingEnabled = !this.debuggingEnabled;
	}

}
