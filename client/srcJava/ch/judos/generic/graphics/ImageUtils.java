package ch.judos.generic.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * some easy functions for images
 */

public class ImageUtils {

	/**
	 * @see #fitInto(Image, int, int)
	 */
	public static Image fitInto(Image image, Dimension size) {
		return fitInto(image, size.width, size.height);
	}

	/**
	 * @param width
	 *            the maximum width for the rescaled image
	 * @param height
	 *            the maximum height for the rescaled image
	 * @return rescaled image to fit into the defined width &amp; height. Aspect
	 *         ratio isn't changed
	 */
	public static Image fitInto(Image image, int width, int height) {
		int imgwidth = image.getWidth(null);
		int imgheight = image.getHeight(null);

		float ratio_is = (float) imgwidth / imgheight;
		float ratio_should = (float) width / height;

		if (ratio_is >= ratio_should) {
			return image.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
		}
		return image.getScaledInstance(-1, height, Image.SCALE_SMOOTH);

	}

	/**
	 * @see #fitOnto(Image,int,int)
	 */
	public static Image fitOnto(Image image, Dimension size) {
		return fitOnto(image, size.width, size.height);
	}

	/**
	 * @param width
	 *            the minimal width for the rescaled image
	 * @param height
	 *            the minimal height for the rescaled image
	 * @return rescaled image to fit onto the defined width &amp; height. Aspect
	 *         ratio is preserved
	 */
	public static Image fitOnto(Image image, int width, int height) {
		int imgwidth = image.getWidth(null);
		int imgheight = image.getHeight(null);

		float ratio_is = (float) imgwidth / imgheight;
		float ratio_should = (float) width / height;

		if (ratio_is >= ratio_should) {
			return image.getScaledInstance(-1, height, Image.SCALE_SMOOTH);
		}
		// else {
		return image.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
		// }

	}

	/**
	 * @param file
	 *            reference to the file
	 * @return the loaded bufferedimage - null if it could not be loaded
	 */
	public static BufferedImage loadBufferedImage(File file) {
		try {
			return ImageIO.read(file);
		}
		catch (IOException e) {
			return null;
		}
	}

	/**
	 * @param file
	 *            path of the file
	 * @return the loaded bufferedimage - null if it could not be loaded
	 */
	public static BufferedImage loadBufferedImage(String file) {
		return loadBufferedImage(new File(file));
	}

	/**
	 * @return the loaded image - may not be fully loaded when the object is
	 *         returned
	 */
	public static Image loadImage(String name) {
		return Toolkit.getDefaultToolkit().getImage(name);
	}

	public static BufferedImage toBufferedImage(Image src) {
		if (src instanceof BufferedImage)
			return (BufferedImage) src;
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		int type = BufferedImage.TYPE_INT_RGB; // other options
		BufferedImage dest = new BufferedImage(w, h, type);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return dest;
	}

	public static int[] getPixels(BufferedImage img, int x, int y, int w, int h,
																int[] pixels) {
		if (w == 0 || h == 0) {
			return new int[0];
		}
		if (pixels == null) {
			pixels = new int[w * h];
		}
		else if (pixels.length < w * h) {
			throw new IllegalArgumentException("pixels array must have a length >= w*h");
		}

		int imageType = img.getType();
		if (imageType == BufferedImage.TYPE_INT_ARGB
				|| imageType == BufferedImage.TYPE_INT_RGB) {
			Raster raster = img.getRaster();
			return (int[]) raster.getDataElements(x, y, w, h, pixels);
		}

		return img.getRGB(x, y, w, h, pixels, 0, w);
	}

	public static void setPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
		if (pixels == null || w == 0 || h == 0) {
			return;
		}
		else if (pixels.length < w * h) {
			throw new IllegalArgumentException("pixels array must have a length >= w*h");
		}

		int imageType = img.getType();
		if (imageType == BufferedImage.TYPE_INT_ARGB
				|| imageType == BufferedImage.TYPE_INT_RGB) {
			WritableRaster raster = img.getRaster();
			raster.setDataElements(x, y, w, h, pixels);
		}
		else {
			img.setRGB(x, y, w, h, pixels, 0, w);
		}
	}

}