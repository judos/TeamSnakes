package old.model.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Options {

	private Logger logger = LogManager.getLogger(getClass());

	private boolean debuggingEnabled = false;

	public boolean isDebuggingEnabled() {
		return this.debuggingEnabled;
	}

	public void toggleDebugOptions() {
		this.debuggingEnabled = !this.debuggingEnabled;
		logger.info("Debug " + (this.debuggingEnabled ? "enabled" : "disabled"));
	}

}
