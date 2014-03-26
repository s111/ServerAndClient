package com.github.groupa.client.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

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

	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);

		Graphics2D g = (Graphics2D) g1;
		g.setTransform(zoomAndPanListener.getCoordTransform());

		if (image != null) {
			BufferedImage bImage = (BufferedImage) image;
			g.drawImage(bImage, null, 0, 0);
		}
	}

}
