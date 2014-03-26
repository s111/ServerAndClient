package com.github.groupa.client.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

public class ZoomAndPanCanvas extends JLabel {

	private static final long serialVersionUID = 1L;

	private ZoomAndPanListener zoomAndPanListener;
	private Image image;

	public ZoomAndPanCanvas() {
		zoomAndPanListener = new ZoomAndPanListener(this);
		addMouseListener(zoomAndPanListener);
		addMouseMotionListener(zoomAndPanListener);
		addMouseWheelListener(zoomAndPanListener);
	}

	public ZoomAndPanCanvas(int minZoomLevel, int maxZoomLevel, double zoomMultiplicationFactor) {
		zoomAndPanListener = new ZoomAndPanListener(this, minZoomLevel, maxZoomLevel, zoomMultiplicationFactor);
		addMouseListener(zoomAndPanListener);
		addMouseMotionListener(zoomAndPanListener);
		addMouseWheelListener(zoomAndPanListener);
	}

	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setTransform(zoomAndPanListener.getCoordTransform());

		if (image != null) {
			BufferedImage bImage = toBufferedImage(image);
			g.drawImage(bImage, null, 0, 0);
		}
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}

	public void setImage(Image image) {
		this.image = image;

	}
}