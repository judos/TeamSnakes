package ch.judos.generic.data;

import java.util.List;


public class RandomJS {
	/**
	 * the generators base
	 */
	protected static java.util.Random r;

	/**
	 * inits the generator
	 */
	protected static void init() {
		if (r == null)
			r = new java.util.Random();
	}

	/**
	 * @return a random object out of the provided ones
	 */
	@SafeVarargs
	public static <T> T getObject(T... ts) {
		init();
		int index = getInt(0, ts.length - 1);
		return ts[index];
	}

	public static <T> T getObject(List<T> list) {
		init();
		int index = getInt(0, list.size() - 1);
		return list.get(index);
	}

	/**
	 * @param max the maximum value (exclusive)
	 * @return a random float
	 */
	public static double getDouble(double max) {
		init();
		return r.nextDouble() * max;
	}

	/**
	 * @return a random generated double value between min and max
	 */
	public static double getDouble(double min, double max) {
		return getDouble(max - min) + min;
	}

	/**
	 * @param max the maximum value (exclusive)
	 * @return a random float
	 */
	public static float getFloat(double max) {
		init();
		return (float) (r.nextDouble() * max);
	}

	/**
	 * @return a random generated float between min and max
	 */
	public static float getFloat(double min, double max) {
		return (float) (getFloat(max - min) + min);
	}

	/**
	 * @param min the minimum value (inclusive)
	 * @param max the maximum value (inclusive)
	 * @return a random int
	 */
	public static int getInt(int min, int max) {
		init();
		return r.nextInt(max - min + 1) + min;
	}

	/**
	 * @param max the maximum value (inclusive)
	 * @return a random int between 0 (inclusive) and the max (inclusive)
	 */
	public static int getInt(int max) {
		init();
		return r.nextInt(max + 1);
	}

	/**
	 * not exact, but good for values between 1% and 99%
	 */
	public static boolean getTrueWithProb(int percentage) {
		return getDouble(100) <= percentage;
	}

	public static boolean getBoolean() {
		return getInt(1) == 1;
	}

	public static <T extends Enum<T>> T getEnum(Class<T> clazz) {
		try {
			@SuppressWarnings("unchecked")
			T[] values = (T[]) clazz.getMethod("values").invoke(null);
			return RandomJS.getObject(values);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
