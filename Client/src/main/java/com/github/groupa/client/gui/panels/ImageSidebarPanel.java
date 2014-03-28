package com.github.groupa.client.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.DisplayedImageChangedEvent;
import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.gui.ActiveImage;
import com.google.common.eventbus.Subscribe;

public class ImageSidebarPanel implements SidebarPanel {
	private JPanel panel = new JPanel();

	private ActiveImage activeImage;

	private JTextField descriptionField;

	private ButtonGroup ratingButtonGroup;

	private JRadioButton ratingButtons[];

	private DefaultListModel<Object> tagListModel;

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
		setUpTagList();
	}

	private void setUpPanel() {
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}

	private void setUpTagList() {
		panel.add(new JLabel("Tags"), "wrap");

		tagListModel = new DefaultListModel<>();

		JList<Object> jList = new JList<>(tagListModel);

		JScrollPane scrollPane = new JScrollPane(jList);

		panel.add(scrollPane, "span 3, grow, push");
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

		addEditRatingButtonListener();
	}

	private void addEditRatingButtonListener() {
		editRatingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				ImageObject image = activeImage.getImage();

				if (image == null) {
					return;
				}

				if (savingRating) {
					savingRating = false;

					editRatingButton.setText("Edit");

					for (int i = 0; i < 5; i++) {
						ratingButtons[i].setEnabled(false);

						if (ratingButtons[i].isSelected()) {
							image.rate(i + 1);
						}
					}

				} else {
					savingRating = true;

					editRatingButton.setText("Save");

					for (int i = 0; i < 5; i++) {
						ratingButtons[i].setEnabled(true);
					}
				}
			}
		});
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

		addEditDescriptionButtonListener();
	}

	private void addEditDescriptionButtonListener() {
		editDescriptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ImageObject image = activeImage.getImage();

				if (image == null) {
					return;
				}

				if (savingDescription) {
					savingDescription = false;

					image.describe(descriptionField.getText());

					editDescriptionButton.setText("Edit");
					descriptionField.setEnabled(false);
					descriptionField.setBorder(BorderFactory
							.createEmptyBorder());
					descriptionField.setBackground(UIManager
							.getColor("Panel.background"));
				} else {
					savingDescription = true;

					editDescriptionButton.setText("Save");
					descriptionField.requestFocus();
					descriptionField.setEnabled(true);
					descriptionField.setBorder(BorderFactory
							.createLineBorder(Color.BLACK));
				}
			}
		});
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
		updateFields(event.getImageObject());
	}

	private void updateFields(ImageObject image) {
		descriptionField.setText(image.getDescription());

		ratingButtonGroup.clearSelection();

		int rating = image.getRating() - 1;

		if (rating >= 0) {
			ratingButtons[rating].setSelected(true);
		}

		tagListModel.clear();

		for (String tag : image.getTags()) {
			tagListModel.addElement(tag);
		}
	}
}
