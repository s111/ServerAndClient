package com.github.groupa.client.gui.panels;

import java.awt.Color;

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
		panel.add(new JButton("Edit"), "wrap");
	}

	private void setUpDescriptionField() {
		panel.add(new JLabel("Description"), "wrap");

		descriptionField = new JTextField("description");
		descriptionField.setEnabled(false);
		descriptionField.setBorder(BorderFactory.createEmptyBorder());
		descriptionField.setDisabledTextColor(Color.BLACK);
		descriptionField.setBackground(UIManager.getColor("Panel.background"));

		panel.add(descriptionField, "span 2, growx, pushx, wmax 160");
		panel.add(new JButton("Edit"), "wrap");
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

		if (rating > 0) {
			ratingButtons[rating].setSelected(true);
		}

		tagListModel.clear();

		for (String tag : image.getTags()) {
			tagListModel.addElement(tag);
		}
	}
}
