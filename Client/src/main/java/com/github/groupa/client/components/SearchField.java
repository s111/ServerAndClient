package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.groupa.client.Library;

public class SearchField implements ActionListener {
	private JPanel panel;
	
	private JTextField searchField;

	private JButton searchButton;

	private Library library;
	public SearchField(Library library) {
		this.library = library;
		panel = new JPanel();

		searchField = new JTextField(8);
		searchButton = new JButton("Search");
		
		searchButton.addActionListener(this);

		panel.add(searchField);
		panel.add(searchButton);
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = searchField.getText();
		if (text == null || text == "") JOptionPane.showInputDialog("Nothing was entered!");
		
		library.addConstraint(Library.ConstraintType.HAS_TAG, text);
		
	}
}
