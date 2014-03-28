package com.github.groupa.client.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

	private ZoomAndPanListener zoomAndPanListener;

	private Image image;

	public ImagePanel() {
		zoomAndPanListener = new ZoomAndPanListener(this);
		addMouseListener(zoomAndPanListener);
		addMouseMotionListener(zoomAndPanListener);
		addMouseWheelListener(zoomAndPanListener);
	}

	public void setImage(Image image) {
		this.image = image;

		repaint();
	}

	public Image getImage() {
		return image;
	}

	@Override
	public void paintComponent(Graphics g) {
		setOpaque(false);
		Graphics2D g2d = (Graphics2D) g.create();

		AffineTransform tx = zoomAndPanListener.getCurrentTransform();

		g2d.drawImage(image, tx, null);
		g2d.dispose();

	}
	// @Override
	// public void paintComponent(Graphics g1) {
	// super.paintComponent(g1);
	//
	// Graphics2D g = (Graphics2D) g1.create();
	// g.setTransform(zoomAndPanListener.getCoordTransform());
	// if (image != null) {
	//
	// BufferedImage bImage = (BufferedImage) image;
	// g.drawImage(bImage, null, 0, 0);
	// }
	// }

}
