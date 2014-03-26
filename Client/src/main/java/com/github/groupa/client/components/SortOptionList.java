package com.github.groupa.client.components;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SortOptionList {
	private final Dimension SIZE = new Dimension(80, 20);

	private String[] sortingOptions = { "date taken", "test", "test1" };

	private JComboBox<String> comboBox;
	private JPanel panel;

	public SortOptionList() {
		panel = new JPanel();
		comboBox = new JComboBox<String>(sortingOptions);

		comboBox.setSelectedIndex(0);

		setUpPanel();
	}

	public JPanel getPanel() {
		return panel;
	}

	private void setUpPanel() {
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setMaximumSize(SIZE);
		panel.setMinimumSize(SIZE);

		panel.add(comboBox);
	}

}
