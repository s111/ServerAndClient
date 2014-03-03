package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.github.groupa.client.Library;

public class ImageRater {
	private static final int MAX_RATING = 5;

	private JRadioButton buttons[] = new JRadioButton[MAX_RATING];
	private String names[] = { "1", "2", "3", "4", "5" };
	private JPanel panel = new JPanel();

	private Library library;

	public ImageRater(Library library) {
		this.library = library;
		
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
			final int rating = i + 1;

			buttons[i] = new JRadioButton(names[i]);
			buttons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent action) {
					JOptionPane.showMessageDialog(null, "You Rated " + rating + "!");
					
					library.getImage().rate(rating);
				}
			});
		}
	}
}