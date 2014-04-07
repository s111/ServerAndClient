package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.CloseAction;

import net.miginfocom.swing.MigLayout;

public class EditMetadataPanel {
	private JPanel panel = new JPanel();

	private JButton saveButton;
	private JButton closeButton;

	private ButtonGroup ratingButtonGroup;

	private JRadioButton ratingButtons[];

	private JTextField descriptionField;

	private JList<String> tagList;

	public EditMetadataPanel() {
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		setUpDescriptionField();
		setUpRatingButtons();
		setUpTagList();
		setUpCloseButton();
		setUpSaveButton();
	}

	public JPanel getPanel() {
		return panel;
	}

	private void setUpSaveButton() {
		saveButton = new JButton("Save");

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		panel.add(saveButton, "wrap");
	}

	private void setUpCloseButton() {
		closeButton = new JButton("Close");

		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		panel.add(closeButton);
	}

	private void setUpDescriptionField() {
		JLabel descriptionLabel = new JLabel("Description");
		descriptionField = new JTextField();

		panel.add(descriptionLabel, "wrap");
		panel.add(descriptionField, "span, growx, pushx, wmax 250");
	}

	private void setUpRatingButtons() {
		JLabel ratingLabel = new JLabel("Rating");

		ratingButtons = new JRadioButton[5];
		ratingButtonGroup = new ButtonGroup();

		JPanel rater = new JPanel();

		for (int i = 0; i < 5; i++) {
			JLabel jLabel = new JLabel(Integer.toString(i + 1));
			ratingButtons[i] = new JRadioButton();
			ratingButtonGroup.add(ratingButtons[i]);

			rater.add(ratingButtons[i]);
			rater.add(jLabel);
		}

		panel.add(ratingLabel, "wrap");
		panel.add(rater, "span");
	}

	private void setUpTagList() {
		JLabel tagLabel = new JLabel("Tags");
		
		DefaultListModel<String> tagListModel = new DefaultListModel<String>(); 
		tagList = new JList<String>(tagListModel);
		
		JScrollPane scrollPane = new JScrollPane(tagList);

		panel.add(tagLabel, "wrap");
		panel.add(scrollPane, "span, wmax 250");
	}
}
