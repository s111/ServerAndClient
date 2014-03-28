package com.github.groupa.client.components;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class ZoomAndPanListener implements MouseListener, MouseMotionListener, MouseWheelListener {

	private Component targetComponent;

	private double currentX;
	private double currentY;
	private double previousX;
	private double previousY;
	private double zoom = 1;

	public ZoomAndPanListener(Component targetComponent) {
		this.targetComponent = targetComponent;
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
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			incrementZoom(.1 * -(double) e.getWheelRotation());
		}
	}

	private void moveCamera(MouseEvent e) {
		Point2D adjPreviousPoint = getTranslatedPoint(previousX, previousY);
		Point2D adjNewPoint = getTranslatedPoint(e.getX(), e.getY());

		double newX = adjNewPoint.getX() - adjPreviousPoint.getX();
		double newY = adjNewPoint.getY() - adjPreviousPoint.getY();

		previousX = e.getX();
		previousY = e.getY();

		currentX += newX;
		currentY += newY;

		targetComponent.repaint();
	}

	private void incrementZoom(double amount) {
		if (zoom < 0) {
			zoom = 0.00001;
		}
		zoom += amount;
		zoom = Math.max(0.00001, zoom);
		targetComponent.repaint();
	}

	public AffineTransform getCurrentTransform() {

		AffineTransform tx = new AffineTransform();

		double centerX = (double) targetComponent.getWidth() / 2;
		double centerY = (double) targetComponent.getHeight() / 2;

		tx.translate(centerX, centerY);
		tx.scale(zoom, zoom);
		tx.translate(currentX, currentY);

		return tx;

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
