package com.github.groupa.client.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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

		addActionListeners();
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

	private void addActionListeners() {
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent selectedSort) {
				if (comboBox.getSelectedIndex() == 0)
					JOptionPane.showMessageDialog(null, "You selected sort by date");
				else if (comboBox.getSelectedIndex() == 1)
					JOptionPane.showMessageDialog(null, "You selected test!");
				else if (comboBox.getSelectedIndex() == 2)
					JOptionPane.showMessageDialog(null, "You selected test1!");
			}
		});
	}
}
