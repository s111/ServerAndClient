package com.github.groupa.client.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.ActiveImage;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.DisplayedImageChangedEvent;
import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.gui.TableCellListener;
import com.google.common.eventbus.Subscribe;

public class ImageSidebarPanel implements SidebarPanel {
	private JPanel panel = new JPanel();

	private ActiveImage activeImage;

	private JTextField descriptionField;

	private ButtonGroup ratingButtonGroup;

	private JRadioButton ratingButtons[];

	private DefaultTableModel tagTableModel;

	private JButton editDescriptionButton;

	private JButton editRatingButton;

	private boolean savingDescription = false;
	private boolean savingRating = false;

	@Inject
	public ImageSidebarPanel(ActiveImage activeImage) {
		this.activeImage = activeImage;

		setUpPanel();
		setUpDescriptionField();
		setUpRatingButtons();
		setUpTagTable();
	}

	private void setUpPanel() {
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}

	private void setUpTagTable() {
		tagTableModel = new DefaultTableModel();
		tagTableModel.addColumn("Tags");

		JTable jTable = new JTable(tagTableModel);

		setUpTableCellListener(jTable);

		JScrollPane scrollPane = new JScrollPane(jTable);

		panel.add(scrollPane, "span 3, grow, push");
	}

	private void setUpTableCellListener(JTable jTable) {
		@SuppressWarnings("serial")
		Action action = new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				TableCellListener cell = (TableCellListener) event.getSource();

				int rowCount = tagTableModel.getRowCount();
				int currentRow = cell.getRow();
				int lastRow = rowCount - 1;

				String newValue = (String) cell.getNewValue();
				String oldValue = (String) cell.getOldValue();

				// You are adding a new tag
				if (currentRow == lastRow) {
					if (newValue != null && !newValue.trim().equals("")) {
						Object[] emptyRow = {};
						tagTableModel.addRow(emptyRow);

						activeImage.getImage().addTag(newValue);
					}
				}
				// You are editing a tag
				else {
					// The last row is for new tags only
					if (currentRow == lastRow) {
						return;
					}

					if (newValue == null || newValue.trim().equals("")) {
						tagTableModel.removeRow(cell.getRow());

						// TODO Send tag delete request
					} else if (!newValue.equals(oldValue)) {
						// TODO Send tag update request or delete old tag and
						// add new tag
					}
				}
			}
		};

		new TableCellListener(jTable, action);
	}

	private void setUpRatingButtons() {
		panel.add(new JLabel("Rating"), "wrap");

		ratingButtons = new JRadioButton[5];
		ratingButtonGroup = new ButtonGroup();

		JPanel rater = new JPanel();

		for (int i = 0; i < 5; i++) {
			ratingButtons[i] = new JRadioButton();
			ratingButtons[i].setEnabled(false);
			ratingButtonGroup.add(ratingButtons[i]);

			rater.add(ratingButtons[i]);
		}

		panel.add(rater, "span 2");

		editRatingButton = new JButton("Edit");

		panel.add(editRatingButton, "wrap");

		addEditRatingListeners();
	}

	private void addEditRatingListeners() {
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ImageObject image = activeImage.getImage();

				if (image == null) {
					return;
				}

				if (savingRating) {
					int selected = setRatingSaveMode();

					if (selected != -1) {
						image.rate(selected + 1);
					}
				} else {
					setRatingEditMode();
				}
			}
		};

		editRatingButton.addActionListener(actionListener);

		for (JRadioButton radioButton : ratingButtons) {
			radioButton.addActionListener(actionListener);
		}
	}

	private void setUpDescriptionField() {
		panel.add(new JLabel("Description"), "wrap");

		descriptionField = new JTextField();
		descriptionField.setEnabled(false);
		descriptionField.setBorder(BorderFactory.createEmptyBorder());
		descriptionField.setDisabledTextColor(Color.BLACK);
		descriptionField.setBackground(UIManager.getColor("Panel.background"));

		panel.add(descriptionField, "span 2, growx, pushx, wmax 160");

		editDescriptionButton = new JButton("Edit");

		panel.add(editDescriptionButton, "wrap");

		addEditDescriptionListeners();
	}

	private void addEditDescriptionListeners() {
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ImageObject image = activeImage.getImage();

				if (image == null) {
					return;
				}

				if (savingDescription) {
					setDescriptionSaveMode();

					image.describe(descriptionField.getText());
				} else {
					setDescriptionEditMode();
				}
			}
		};

		editDescriptionButton.addActionListener(actionListener);
		descriptionField.addActionListener(actionListener);
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Subscribe
	public void imageInfoChanged(ImageInfoChangedEvent event) {
		ImageObject image = event.getImageObject();
		if (image.equals(activeImage.getImage())) {
			updateFields(image);
		}
	}

	@Subscribe
	public void displayedImageChangeListener(DisplayedImageChangedEvent event) {
		ImageObject image = event.getImageObject();

		setRatingSaveMode();
		setDescriptionSaveMode();
		updateFields(image);
	}

	private void updateFields(ImageObject image) {
		descriptionField.setText(image.getDescription());

		ratingButtonGroup.clearSelection();

		int rating = image.getRating() - 1;

		if (rating >= 0) {
			ratingButtons[rating].setSelected(true);
		}

		updateTagTable(image);
	}

	private void updateTagTable(ImageObject image) {
		tagTableModel.setRowCount(0);

		for (String tag : image.getTags()) {
			Object[] row = { tag };

			tagTableModel.addRow(row);
		}

		Object[] emptyRow = {};

		tagTableModel.addRow(emptyRow);
	}

	private void setDescriptionSaveMode() {
		savingDescription = false;

		editDescriptionButton.setText("Edit");
		descriptionField.setEnabled(false);
		descriptionField.setBorder(BorderFactory.createEmptyBorder());
		descriptionField.setBackground(UIManager.getColor("Panel.background"));
	}

	private void setDescriptionEditMode() {
		savingDescription = true;

		editDescriptionButton.setText("Save");
		descriptionField.requestFocus();
		descriptionField.setEnabled(true);
		descriptionField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	private int setRatingSaveMode() {
		int selected = -1;

		savingRating = false;

		editRatingButton.setEnabled(true);

		for (int i = 0; i < 5; i++) {
			ratingButtons[i].setEnabled(false);

			if (ratingButtons[i].isSelected()) {
				selected = i;
			}
		}

		return selected;
	}

	private void setRatingEditMode() {
		savingRating = true;

		editRatingButton.setEnabled(false);

		for (int i = 0; i < 5; i++) {
			ratingButtons[i].setEnabled(true);
		}
	}
}
