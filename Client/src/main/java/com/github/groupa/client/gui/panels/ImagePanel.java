package com.github.groupa.client.gui.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import com.github.groupa.client.gui.ZoomAndPanListener;

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

		zoomAndPanListener.setImage(image);
		zoomAndPanListener.resetZoom();
		zoomAndPanListener.resetPan();

		repaint();
	}

	public Image getImage() {
		return image;
	}

	@Override
	public void paintComponent(Graphics g) {
		setOpaque(false);
		Graphics2D g2d = (Graphics2D) g.create();

		if (image == null) {
			return;
		}

		AffineTransform transformer = zoomAndPanListener.getCurrentTransform();

		g2d.drawImage(image, transformer, null);
		g2d.dispose();
	}
}