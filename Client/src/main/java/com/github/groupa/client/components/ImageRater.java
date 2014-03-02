package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ImageRater implements ActionListener {
	private static final int MAX_RATING = 5;

	private JRadioButton buttons[] = new JRadioButton[MAX_RATING];
	private String names[] = { "1", "2", "3", "4", "5" };
	private JPanel panel = new JPanel();

	public ImageRater() {
		createRadioButtons();
		addRadioButtonsToGroup();

		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		
		addRadioButtonsToPanel();
	}
	
	public JPanel getPanel() {
		return panel;
	}

	private void addRadioButtonsToPanel() {
		for (int i = 0; i < MAX_RATING; i++) {
			panel.add(buttons[i]);
			if (i != MAX_RATING - 1)
				panel.add(Box.createHorizontalGlue());
		}
	}

	private void addRadioButtonsToGroup() {
		ButtonGroup ratingGroup = new ButtonGroup();

		for (int i = 0; i < MAX_RATING; i++) {
			ratingGroup.add(buttons[i]);
		}
	}

	private void createRadioButtons() {
		for (int i = 0; i < MAX_RATING; i++) {
			buttons[i] = new JRadioButton(names[i]);
			buttons[i].addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttons[0])
			JOptionPane.showMessageDialog(null, "You Rated 1!");
		else if (e.getSource() == buttons[1])
			JOptionPane.showMessageDialog(null, "You Rated 2!");
		else if (e.getSource() == buttons[2])
			JOptionPane.showMessageDialog(null, "You Rated 3!");
		else if (e.getSource() == buttons[3])
			JOptionPane.showMessageDialog(null, "You Rated 4!");
		else if (e.getSource() == buttons[4])
			JOptionPane.showMessageDialog(null, "You Rated 5!");
	}
}