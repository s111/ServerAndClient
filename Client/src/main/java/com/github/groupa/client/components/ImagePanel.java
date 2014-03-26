package com.github.groupa.client.components;

import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel {
	private JPanel panel;

	private ZoomAndPanCanvas imageCanvas;

	public ImagePanel() {
		imageCanvas = new ZoomAndPanCanvas();
		setUpPanel();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setImage(Image image) {
		imageCanvas.setImage(image);
	}

	private void setUpPanel() {
		panel = new JPanel();

		panel.add(imageCanvas);
	}
}
