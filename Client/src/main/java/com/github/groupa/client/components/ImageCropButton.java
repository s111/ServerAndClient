package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.github.groupa.client.views.ImageView;

public class ImageCropButton {
	private JButton button;

	private ImageView imageView;

	public ImageCropButton(ImageView imageView) {
		this.imageView = imageView;

		button = new JButton("Crop Image");
		addActionListeners();
	}

	public JButton getButton() {
		return button;
	}

	private void addActionListeners() {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent action) {
				JOptionPane.showMessageDialog(null,
						"You clicked the crop button!");
			}
		});
	}
}
