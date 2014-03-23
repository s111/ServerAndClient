package com.github.groupa.client.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtils {
	public static BufferedImage resizeImage(Image image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = resizedImage.createGraphics();
		graphics.drawImage(image, 0, 0, width, height, null);
		graphics.dispose();

		return resizedImage;
	}

	public static BufferedImage zoomImage(Image image, int width, int height,
			float zoom) {
		width *= Math.abs(zoom);
		height *= Math.abs(zoom);

		BufferedImage zoomedInImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = zoomedInImage.createGraphics();
		graphics.drawImage(image, 0, 0, width, height, null);
		graphics.dispose();

		return zoomedInImage;
	}
}
