package ch.judos.generic.control;

import ch.judos.generic.data.StringUtils;

/**
 * @author Julian Schelker
 * @version 1.1 / 22.02.2013
 * @since 07.11.2011
 */
public class TimerJS {

	/**
	 * time of the start
	 */
	protected long ns;
	/**
	 * name of the timer
	 */
	protected String name;

	/**
	 * create a timer
	 */
	public TimerJS() {
		this("unnamed timer");
	}

	/**
	 * create a timer with a name
	 */
	public TimerJS(String name) {
		this.name = name;
		start();
	}

	/**
	 * @return hours, minutes and seconds in an array
	 */
	public int[] getHMS() {
		int r[] = new int[3];
		r[2] = (int) getS();
		r[0] = r[2] / 3600;
		r[1] = (r[2] % 3600) / 60;
		r[0] = r[0] % 60;
		return r;
	}

	/**
	 * @return milli seconds since start()
	 */
	public long getMS() {
		return (System.nanoTime() - this.ns) / 1000000;
	}

	/**
	 * @return nano seconds since start()
	 */
	public long getNS() {
		return System.nanoTime() - this.ns;
	}

	/**
	 * @return micro seconds since start()
	 */
	public long getQS() {
		return (System.nanoTime() - this.ns) / 1000;
	}

	/**
	 * @return seconds since start()
	 */
	public long getS() {
		return (System.nanoTime() - this.ns) / 1000000000L;
	}

	/**
	 * displays the time since start() in the format ((h"h"m)m"m")ss"s"
	 */
	public void printHMS() {
		int[] t = getHMS();
		boolean m2 = false;
		if (t[0] > 0) {
			m2 = true;
			System.out.print(t[0] + "h");
		}
		if (m2)
			System.out.print(StringUtils.extendLeftWith(t[1], 2, "0") + "m");
		else if (t[1] > 0)
			System.out.print(t[1] + "m");
		System.out.println(StringUtils.extendLeftWith(t[2], 2, "0") + "s");
	}

	/**
	 * displays the time since start() in System.out (milli seconds)
	 */
	public void printMS() {
		printWithName(getMS(), "ms");
	}

	/**
	 * displays the time since start() in System.out (milli seconds)
	 */
	public void printMS(String prefix) {
		printWithPrefix(prefix, getMS(), "ms");
	}

	/**
	 * displays the time since start() in System.out (nano seconds)
	 */
	public void printNS() {
		printWithName(getNS(), "ns");
	}

	/**
	 * displays the time since start() in System.out (micro seconds)
	 */
	public void printQS() {
		printWithName(getQS(), "qs");
	}

	/**
	 * displays the time since start() in System.out (seconds)
	 */
	public void printS() {
		printWithName(getS(), "sec.");
	}

	private void printWithName(long nr, String unit) {
		System.out.println(this.name + ": " + nr + " " + unit);
	}

	private void printWithPrefix(String prefix, long nr, String unit) {
		System.out.println(prefix + ": " + nr + " " + unit);
	}

	/**
	 * starts counting the time
	 */
	public void start() {
		this.ns = System.nanoTime();
	}

}
