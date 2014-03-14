package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;

public class ImageDescriptionButton implements ActionListener {
	private JButton button;

	private Library library;

	public ImageDescriptionButton(Library library) {
		this.library = library;

		button = new JButton("Add Description");
		button.addActionListener(this);
	}

	public JButton getButton() {
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ImageObject image = library.getActiveImage();

		if (image == null)
			return;

		String description = JOptionPane
				.showInputDialog("Please type your description of this image.");

		if (description == null)
			return;

		image.describe(description);
	}
}
