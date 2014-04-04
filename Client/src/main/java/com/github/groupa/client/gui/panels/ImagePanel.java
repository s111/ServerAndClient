package com.github.groupa.client.gui.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ImagePanel extends JComponent {
	private static final double SCALE_MAX = 2.8;
	private static final double SCALE_MIN = 0.1;

	private BufferedImage image;

	private double imageOffsetX;
	private double imageOffsetY;

	private double oldScale = 1;
	private double scale = 1;
	private double scaleWhereImageWillBeOutOfBounds;

	private int rotation;

	private boolean invertSize;
	private boolean resizeImageToFitPanelOnNextRepaint;

	public ImagePanel() {
		ImageMouseListener imageMouseListener = new ImageMouseListener();
		ImagePanelListener imageComponentListener = new ImagePanelListener();

		addMouseListener(imageMouseListener);
		addMouseMotionListener(imageMouseListener);
		addMouseWheelListener(imageMouseListener);
		addComponentListener(imageComponentListener);
	}

	public void setImage(BufferedImage image) {
		this.image = image;

		resetImage();
		repaint();
	}

	public void rotateCW() {
		rotate(90);
	}

	public void rotateCCW() {
		rotate(-90);
	}

	private void rotate(int dTheta) {
		invertSize = !invertSize;

		rotation += dTheta;

		resetImagePosition();
		resetImageScaling();
		repaint();
	}

	private void resetImage() {
		resetImagePosition();
		resetImageScaling();
		resetRotation();
	}

	private void resetImagePosition() {
		imageOffsetX = 0;
		imageOffsetY = 0;
	}

	private void resetImageScaling() {
		oldScale = 1;
		scale = 1;

		resizeImageToFitPanelOnNextRepaint = true;
	}

	private void resetRotation() {
		invertSize = false;
		rotation = 0;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		if (image == null) {
			return;
		}

		calculateScaleWhereImageWillBeOutOfBounds();
		checkOffsetBoundaries();

		if (resizeImageToFitPanelOnNextRepaint) {
			resetImagePosition();
			resizeToPanel();

			resizeImageToFitPanelOnNextRepaint = false;
		}

		AffineTransform imageTransformer = createTransformer();

		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.drawImage(image, imageTransformer, null);
	}

	/**
	 * Should be called on next repaint not directly!
	 */
	private void resizeToPanel() {
		if (getImageWidth() > getWidth() || getImageHeight() > getHeight()) {
			// Scale the image down to just below panel size
			setScale(scaleWhereImageWillBeOutOfBounds * 0.99);
		} else {
			resetImageScaling();
		}
	}

	private void calculateScaleWhereImageWillBeOutOfBounds() {
		/*
		 * We use getImageWidth and getImageHeight here because the image might
		 * be rotated. The width and height then needs to be swapped.
		 */
		double widthScale = (double) getWidth() / getImageWidth();
		double heightScale = (double) getHeight() / getImageHeight();

		/*
		 * Here we select the lowest of the two as we want to be able to pan as
		 * soon something is outside the panel view.
		 */
		scaleWhereImageWillBeOutOfBounds = Math.min(widthScale, heightScale);
	}

	private AffineTransform createTransformer() {
		AffineTransform imageTransformer = new AffineTransform();

		// We need the orignal images width and height here!
		double scaledImageWidth = image.getWidth() * scale;
		double scaledImageHeight = image.getHeight() * scale;

		double centerOfPanelRelativeToImageAnchorX = (getWidth() - scaledImageWidth) / 2;
		double centerOfPanelRelativeToImageAnchorY = (getHeight() - scaledImageHeight) / 2;

		double distanceFromTopLeftX = imageOffsetX
				+ centerOfPanelRelativeToImageAnchorX;
		double distanceFromTopLeftY = imageOffsetY
				+ centerOfPanelRelativeToImageAnchorY;

		/*
		 * This function basically moves the coordiante system from the topleft
		 * corner of the panel to the x and y specified. Here x and y is set so
		 * that the images center is placed over the panels center.
		 */
		imageTransformer.translate(distanceFromTopLeftX, distanceFromTopLeftY);

		imageTransformer.scale(scale, scale);

		imageTransformer.rotate(Math.toRadians(rotation), image.getWidth() / 2,
				image.getHeight() / 2);

		return imageTransformer;
	}

	private void checkOffsetBoundaries() {
		// We need the rotated images width and height here!
		double scaledImageWidth = getImageWidth() * scale;
		double scaledImageHeight = getImageHeight() * scale;

		double topLimit = -scaledImageHeight / 2;
		double leftLimit = -scaledImageWidth / 2;

		double bottomLimit = scaledImageHeight / 2;
		double rightLimit = scaledImageWidth / 2;

		imageOffsetY = Math.max(topLimit, imageOffsetY);
		imageOffsetX = Math.max(leftLimit, imageOffsetX);

		imageOffsetY = Math.min(bottomLimit, imageOffsetY);
		imageOffsetX = Math.min(rightLimit, imageOffsetX);
	}

	private int getImageWidth() {
		return invertSize ? image.getHeight() : image.getWidth();
	}

	private int getImageHeight() {
		return invertSize ? image.getWidth() : image.getHeight();
	}

	private void setScale(double newScale) {
		oldScale = scale;
		scale = newScale;

		restrainScale();
		handleZoomOut();
	}

	private void restrainScale() {
		scale = Math.max(SCALE_MIN, scale);
		scale = Math.min(SCALE_MAX, scale);
	}

	private void handleZoomOut() {
		boolean isZoomingOut = scale < oldScale;

		if (isZoomingOut && oldScale >= scaleWhereImageWillBeOutOfBounds) {
			double scaleDifference = oldScale
					- scaleWhereImageWillBeOutOfBounds;

			/*
			 * Calculates how fast it should converge towards the center of the
			 * panel. It will move faster as it comes closer. If the
			 * scaleDifference is only 1 then 100% of the distance will be
			 * covered if zoomed out. If the scaleDifference is 2 then 50% of
			 * the distance will be covered and so on. scaleDifference telling
			 * you how many "clicks" the image is zoomed out.
			 */
			float fractionOfDistanceTowardsCenterToMove = 1f / ((int) (scaleDifference * 10) + 1);

			imageOffsetX = (int) (imageOffsetX - imageOffsetX
					* fractionOfDistanceTowardsCenterToMove);
			imageOffsetY = (int) (imageOffsetY - imageOffsetY
					* fractionOfDistanceTowardsCenterToMove);
		}
	}

	private class ImageMouseListener extends MouseAdapter {
		private static final double SCALE_TICK = 0.1;

		private int startOffsetX;
		private int startOffsetY;

		@Override
		public void mousePressed(MouseEvent event) {
			super.mousePressed(event);

			startOffsetX = event.getX();
			startOffsetY = event.getY();
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			super.mouseDragged(event);

			boolean imageIsWithinBounds = scale < scaleWhereImageWillBeOutOfBounds;

			if (imageIsWithinBounds) {
				return;
			}

			int newOffsetX = event.getX() - startOffsetX;
			int newOffsetY = event.getY() - startOffsetY;

			startOffsetX += newOffsetX;
			startOffsetY += newOffsetY;

			imageOffsetX += newOffsetX;
			imageOffsetY += newOffsetY;

			repaint();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent event) {
			super.mouseWheelMoved(event);

			double newScale = scale - (SCALE_TICK * event.getWheelRotation());

			setScale(newScale);
			repaint();
		}
	}

	private class ImagePanelListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);

			resizeImageToFitPanelOnNextRepaint = true;

			repaint();
		}
	}
}