package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ImageDescriptionButton implements ActionListener {
	private JButton button;
	private String description;

	public ImageDescriptionButton() {
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
		if (e.getSource() == button)
			this.description = JOptionPane
					.showInputDialog("Please type your description of this image.");

		JOptionPane.showMessageDialog(null, "Your description of this image:\n"
				+ description);
	}

}
