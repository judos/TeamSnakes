package ch.judos.generic.control;

/**
 * Creates a action counter which calculates the frequency of the action
 * performed periodically
 */
public class ActionCounter {

	/**
	 * current number of executions
	 */
	protected int done;
	/**
	 * milliseconds of the last check
	 */
	protected long time;
	/**
	 * every checkTime the actionRate is calculated
	 */
	protected int checkTime;

	/**
	 * the current actionRate
	 */
	protected int actionRate;

	/**
	 * Periodically calculates the actionRate every second.
	 */
	public ActionCounter() {
		this(1000);
	}

	/**
	 * @param checkTimeMS periodically calculates the actionRate after checkTimeMS
	 *                    milliseconds.
	 */
	public ActionCounter(int checkTimeMS) {
		this.checkTime = checkTimeMS;
		this.done = 0;
		this.time = System.currentTimeMillis();
		this.actionRate = 0;
	}

	/**
	 * counts the current operation as an action
	 *
	 * @return whether actionRate has just been recalculated <br>
	 * use getActionRate() to get the frequency information
	 */
	public boolean action() {
		this.done++;
		if (System.currentTimeMillis() - this.time >= this.checkTime) {
			this.actionRate = Math.round(((float) this.done) / this.checkTime * 1000);
			this.time = System.currentTimeMillis();
			this.done = 0;
			return true;
		}
		return false;
	}

	/**
	 * @return current action rate
	 */
	public int getActionRate() {
		return this.actionRate;
	}

}
