package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.views.ImageView;

public class ImageTag implements ActionListener {
	private JButton button;

	private ImageView imageView;

	public ImageTag(ImageView imageView) {
		this.imageView = imageView;

		button = new JButton("Add Tag");
		button.addActionListener(this);
	}

	public JButton getTagButton() {
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ImageObject image = imageView.getActiveImageObject();

		if (image == null)
			return;

		String tag = JOptionPane.showInputDialog("Enter a tag to this image");

		if (tag == null)
			return;

		image.addTag(tag);
	}
}
