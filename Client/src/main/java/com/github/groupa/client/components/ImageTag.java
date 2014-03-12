package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;

public class ImageTag implements ActionListener {
	private JButton button;

	private Library library;

	public ImageTag(Library library) {
		this.library = library;

		button = new JButton("Add Tag");
		button.addActionListener(this);
	}

	public JButton getTagButton() {
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ImageObject image = library.getImage();

		if (image == null)
			return;

		String tag = JOptionPane.showInputDialog("Enter a tag to this image");

		if (tag == null)
			return;

		image.addTag(tag);
	}
}