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

		zoomAndPanListener.setImageInfo(image);

		repaint();
	}

	public Image getImage() {
		return image;
	}

	@Override
	public void paintComponent(Graphics g) {
		setOpaque(false);
		Graphics2D g2d = (Graphics2D) g.create();

		AffineTransform transformer = zoomAndPanListener.getCurrentTransform();

		g2d.drawImage(image, transformer, null);
		g2d.dispose();

	}
}
