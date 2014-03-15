package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.views.ImageView;

public class ImageDescriptionButton implements ActionListener {
	private JButton button;

	private ImageView imageView;

	public ImageDescriptionButton(ImageView imageView) {
		this.imageView = imageView;

		button = new JButton("Add Description");
		button.addActionListener(this);
	}

	public JButton getButton() {
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ImageObject image = imageView.getActiveImageObject();

		if (image == null)
			return;

		String description = JOptionPane
				.showInputDialog("Please type your description of this image.");

		if (description == null)
			return;

		image.describe(description);
	}
}
