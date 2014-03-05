package com.github.groupa.client.components;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchField {
	JPanel panel;

	JTextField searchField;

	JButton searchButton;

	public SearchField() {
		panel = new JPanel();

		searchField = new JTextField(8);
		searchButton = new JButton("Search");

		panel.add(searchField);
		panel.add(searchButton);
	}

	public JPanel getPanel() {
		return panel;
	}
}
