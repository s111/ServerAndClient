package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class EditMetadataPanel {
	private JPanel panel = new JPanel();

	private JButton editButton;
	private JButton closeButton;

	public EditMetadataPanel() {
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);
		
		setUpEditButton();;
		setUpCloseButton();
	}

	public JPanel getPanel() {
		return panel;
	}

	private void setUpEditButton() {
		editButton = new JButton("Edit");

		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		panel.add(editButton);
	}

	private void setUpCloseButton() {
		closeButton = new JButton("Close");

		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		panel.add(closeButton);
	}
}
