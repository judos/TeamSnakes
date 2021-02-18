package model.game;

import ch.judos.generic.control.Log;

public class Options {

	private boolean debuggingEnabled = false;

	public boolean isDebuggingEnabled() {
		return this.debuggingEnabled;
	}

	public void toggleDebugOptions() {
		this.debuggingEnabled = !this.debuggingEnabled;
		Log.info("Debug " + (this.debuggingEnabled ? "enabled" : "disabled"));
	}

}
