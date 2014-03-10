package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;

public class ImageDescriptionButton implements ActionListener {
	private JButton button;
	private String description;

	private Library library;

	public ImageDescriptionButton(Library library) {
		this.library = library;

		createCommentButton();
	}

	public JButton getButton() {
		return button;
	}

	private void createCommentButton() {
		button = new JButton("Add Description");
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ImageObject image = library.getImage();

		if (image == null)
			return;

		description = JOptionPane
				.showInputDialog("Please type your description of this image.");

		if (description == null)
			return;

		image.describe(description);
	}
}
