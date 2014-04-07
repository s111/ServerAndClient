package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class EditMetadataPanel {
	private JPanel panel = new JPanel();

	private JButton editButton;
	private JButton cancelButton;

	private ButtonGroup ratingButtonGroup;

	private JRadioButton ratingButtons[];

	private JTextField descriptionField;

	private JTable tagTable;

	private DefaultTableModel tagTableModel;

	public EditMetadataPanel() {
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		setUpDescriptionField();
		setUpRatingButtons();
		setUpTagTable();
		setUpEditButton();
		setUpCancelButton();
	}

	public JPanel getPanel() {
		return panel;
	}

	private void setUpEditButton() {
		editButton = new JButton("Edit");

		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				descriptionField.setEnabled(true);
				tagTable.setEnabled(true);

				for (int i = 0; i < 5; i++)
					ratingButtons[i].setEnabled(true);
				
				cancelButton.setEnabled(true);
			}
		});

		panel.add(editButton);
	}

	private void setUpCancelButton() {
		cancelButton = new JButton("Cancel");

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				descriptionField.setEnabled(false);
				tagTable.setEnabled(false);

				for (int i = 0; i < 5; i++)
					ratingButtons[i].setEnabled(false);
				cancelButton.setEnabled(false);
			}
		});

		cancelButton.setEnabled(false);

		panel.add(cancelButton, "wrap");
	}

	private void setUpDescriptionField() {
		JLabel descriptionLabel = new JLabel("Description");
		descriptionField = new JTextField();

		descriptionField.setEnabled(false);

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
			ratingButtons[i].setEnabled(false);
			ratingButtonGroup.add(ratingButtons[i]);

			rater.add(ratingButtons[i]);
			rater.add(jLabel);
		}

		panel.add(ratingLabel, "wrap");
		panel.add(rater, "span");
	}

	private void setUpTagTable() {
		tagTableModel = new DefaultTableModel();
		tagTableModel.addColumn("Tags");

		tagTable = new JTable(tagTableModel);

		JScrollPane scrollPane = new JScrollPane(tagTable);

		tagTable.setEnabled(false);

		panel.add(scrollPane, "span, wmax 250");
	}
}
