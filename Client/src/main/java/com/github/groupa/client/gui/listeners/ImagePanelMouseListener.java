package com.github.groupa.client.gui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import com.github.groupa.client.gui.panels.ImagePanel;

public class ImagePanelMouseListener extends MouseAdapter {
	private ImagePanel imagePanel;

	private double currentX;
	private double currentY;
	private double previousX;
	private double previousY;
	private double zoom = 1;

	public ImagePanelMouseListener(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		previousX = event.getX();
		previousY = event.getY();
	}

	@Override
	public void mouseReleased(MouseEvent event) {
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		moveCamera(event);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		zoom(event);
	}

	public AffineTransform getCurrentTransform() {
		AffineTransform transformer = new AffineTransform();

		// Set upper-left coordinate (0, 0) of image to center of component
		double centerX = (double) imagePanel.getWidth() / 2;
		double centerY = (double) imagePanel.getHeight() / 2;
		transformer.translate(centerX, centerY);

		// Get large images to resize down so they fit the window
		if (imagePanel.getImage().getWidth(null) > imagePanel.getWidth()
				|| imagePanel.getImage().getHeight(null) > imagePanel
						.getHeight()) {
			double ratio;
			if (imagePanel.getImage().getWidth(null) > imagePanel.getImage()
					.getHeight(null)) {
				ratio = (double) imagePanel.getImage().getWidth(null)
						/ imagePanel.getWidth();
			} else {
				ratio = (double) imagePanel.getImage().getHeight(null)
						/ imagePanel.getHeight();
			}

			transformer.scale(zoom / ratio, zoom / ratio);
		}

		transformer.scale(zoom, zoom);

		transformer.translate(currentX, currentY);

		// Paint image to center of component
		transformer.translate(-imagePanel.getImage().getWidth(null) / 2,
				-imagePanel.getImage().getHeight(null) / 2);

		return transformer;

	}

	public void resetZoom() {
		zoom = 1;
	}

	public void resetPan() {
		currentX = 0;
		currentY = 0;
	}

	private void moveCamera(MouseEvent e) {
		if (imagePanel.getImage() == null) {
			return;
		}

		Point2D adjPreviousPoint = getTranslatedPoint(previousX, previousY);
		Point2D adjNewPoint = getTranslatedPoint(e.getX(), e.getY());

		double dx = adjNewPoint.getX() - adjPreviousPoint.getX();
		double dy = adjNewPoint.getY() - adjPreviousPoint.getY();

		previousX = e.getX();
		previousY = e.getY();

		currentX += dx;
		currentY += dy;

		imagePanel.repaint();
	}

	private void zoom(MouseWheelEvent e) {
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			if (zoom < 0) {
				zoom = 0.00001;
			}
			zoom += .1 * -(double) e.getWheelRotation();
			zoom = Math.max(0.00001, zoom);
			imagePanel.repaint();
		}
	}

	// Convert the panel coordinates into the cooresponding coordinates on the
	// translated image.
	private Point2D getTranslatedPoint(double panelX, double panelY) {

		AffineTransform tx = getCurrentTransform();
		Point2D point2d = new Point2D.Double(panelX, panelY);
		try {
			return tx.inverseTransform(point2d, null);
		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
