package com.github.groupa.client.components;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.groupa.client.utils.ImageUtils;

public class ImagePanel {
	private static final String IMAGE_NOT_LOADED = "image not loaded";
	private JPanel panel;

	private JLabel imageLabel;
	private Image image;

	private float currentZoom = 1;

	public ImagePanel() {
		imageLabel = new JLabel(IMAGE_NOT_LOADED);

		setUpPanel();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setImage(Image image) {
		this.image = image;

		resize();
	}

	private void setUpPanel() {
		panel = new JPanel();

		panel.add(imageLabel);

		addPanelListeners();
	}

	private void addPanelListeners() {
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				resize();
			}
		});

		panel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				int wheelRotation = event.getWheelRotation();

				updateCurrentZoom(wheelRotation);

				zoom(currentZoom);
			}
		});
	}

	private void resize() {
		int width = panel.getWidth();
		int height = panel.getHeight();

		if (image == null || width == 0 || height == 0) {
			return;
		}

		Image resizedImage = ImageUtils.resizeImage(image, width, height);

		updateImageLabel(resizedImage);
	}

	private void zoom(float zoom) {
		int width = panel.getWidth();
		int height = panel.getHeight();

		if (image == null || width == 0 || height == 0) {
			return;
		}

		Image zoomedInImage = ImageUtils.zoomImage(image, width, height, zoom);

		updateImageLabel(zoomedInImage);
	}

	private void updateImageLabel(Image image) {
		if (image == null) {
			return;
		}

		imageLabel.setText("");
		imageLabel.setIcon(new ImageIcon(image));
	}

	private void updateCurrentZoom(int wheelRotation) {
		if (wheelRotation < 0) {
			currentZoom += 0.1;
		} else {
			currentZoom -= 0.1;
		}

		if (currentZoom > 2) {
			currentZoom = 2;
		} else if (currentZoom < 0.5f) {
			currentZoom = 0.5f;
		}
	}
}
