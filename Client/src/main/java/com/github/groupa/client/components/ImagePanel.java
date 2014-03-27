package com.github.groupa.client.components;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.groupa.client.gui.panels.Panel;

public class ImagePanel implements Panel {
	private static final String IMAGE_NOT_LOADED = "image not loaded";
	private JPanel panel = new JPanel();

	private JLabel imageLabel;

	public ImagePanel() {
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		imageLabel = new JLabel(IMAGE_NOT_LOADED);

		setUpPanel();
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	public void setImage(Image image) {
		updateImageLabel(image);
	}

	private void setUpPanel() {
		panel.add(Box.createHorizontalGlue());
		panel.add(imageLabel, BorderLayout.CENTER);
		panel.add(Box.createHorizontalGlue());
	}

	private void updateImageLabel(Image image) {
		if (image == null) {
			return;
		}

		imageLabel.setText("");
		imageLabel.setIcon(new ImageIcon(image));
	}
}
