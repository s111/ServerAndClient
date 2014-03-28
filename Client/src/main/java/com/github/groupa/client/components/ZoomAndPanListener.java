package com.github.groupa.client.components;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class ZoomAndPanListener implements MouseListener, MouseMotionListener, MouseWheelListener {

	private Component imagePanel;

	private Image image;

	private double currentX;
	private double currentY;
	private double previousX;
	private double previousY;
	private double zoom = 1;

	public ZoomAndPanListener(Component imagePanel) {
		this.imagePanel = imagePanel;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		previousX = e.getX();
		previousY = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		moveCamera(e);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		zoom(e);
	}

	public AffineTransform getCurrentTransform() {

		AffineTransform transformer = new AffineTransform();

		// Set upper-left coordinate (0, 0) of image to center of component
		double centerX = (double) imagePanel.getWidth() / 2;
		double centerY = (double) imagePanel.getHeight() / 2;
		transformer.translate(centerX, centerY);

		// Get large images to resize down so they fit the window
		if (image.getWidth(null) > imagePanel.getWidth() || image.getHeight(null) > imagePanel.getHeight()) {
			double ratio;
			if (image.getWidth(null) > image.getHeight(null)) {
				ratio = (double) image.getWidth(null) / imagePanel.getWidth();
			} else {
				ratio = (double) image.getHeight(null) / imagePanel.getHeight();
			}

			transformer.scale(zoom / ratio, zoom / ratio);
		}

		transformer.scale(zoom, zoom);

		transformer.translate(currentX, currentY);

		// Paint image to center of component
		transformer.translate(-image.getWidth(null) / 2, -image.getHeight(null) / 2);

		return transformer;

	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void resetZoom() {
		zoom = 1;
	}

	public void resetPan() {
		currentX = 0;
		currentY = 0;
	}

	private void moveCamera(MouseEvent e) {
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
