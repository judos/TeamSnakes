package old;

import ch.judos.generic.control.Log;
import ch.judos.generic.control.TimerJS;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Julian Schelker
 */
public class Assets {

	private static boolean isLoaded = false;
	private static boolean isLoading = false;
	public static Font font;

	public static boolean isLoaded() {
		return isLoaded;
	}

	public static void waitUntilAssetsAreLoaded() {
		synchronized (Assets.class) {
			while (!isLoaded) {
				try {
					Assets.class.wait();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * checked method, synchronous
	 * 
	 * @throws IllegalStateException
	 *             when the assets are already being loaded or loaded
	 */
	public static void load() {
		synchronized (Assets.class) {
			if (isLoading)
				throw new IllegalStateException("Loading the assets is already in progress");
			if (isLoaded)
				throw new IllegalStateException("The assets are already loaded");
			isLoading = true;
		}
		loadAllAssets();
		synchronized (Assets.class) {
			isLoading = false;
			isLoaded = true;
			Assets.class.notifyAll();
		}
	}

	/**
	 * unsynchronized, unchecked method
	 */
	private static void loadAllAssets() {
		TimerJS loadAssestsTimer = new TimerJS();
		font = new Font("Arial", 0, 16);
		Log.info("loading Assets: " + loadAssestsTimer.getMS() + " ms");
	}

	public static BufferedImage load(String name) {
		try {
			return ImageIO.read(new File("data/" + name));
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Dimension dimensionOf(BufferedImage image) {
		return new Dimension(image.getWidth(), image.getHeight());
	}
}
