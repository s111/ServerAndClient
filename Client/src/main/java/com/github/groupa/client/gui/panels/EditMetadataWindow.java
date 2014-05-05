package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.jsonobjects.ImageFull;
import com.github.groupa.client.jsonobjects.ImagesUpdate;
import com.github.groupa.client.servercommunication.ModifyImage;

public class EditMetadataWindow {
	private JPanel panel = new JPanel();

	private JDialog dialog;

	private JButton saveButton;
	private JButton closeButton;
	private JButton deleteTagButton;
	private JButton addTagButton;

	private ButtonGroup ratingButtonGroup;

	private JRadioButton ratingButtons[];

	private JTextField descriptionField;
	private JTextField tagTextField;

	private int selectedTagIndex;
	private int selectedRating;

	private String tagText = null;

	private DefaultListModel<String> tagListModel = new DefaultListModel<String>();

	private JList<String> tagList;

	private ModifyImage modifyImage;

	private ThumbPanel thumbPanel;

	@Inject
	public EditMetadataWindow(ModifyImage modifyImage, ThumbPanel thumbPanel) {
		this.modifyImage = modifyImage;
		this.thumbPanel = thumbPanel;

		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		setUpDescriptionField();
		setUpRatingButtons();
		setUpTagTextField();
		setUpAddTagButton();
		setUpDeleteTagButton();
		setUpTagList();
		setUpSaveButton();
		setUpCloseButton();

		setUpMetadataDialog();
	}

	public JPanel getPanel() {
		return panel;
	}

	public JDialog getDialog() {
		return dialog;
	}

	private void setUpMetadataDialog() {
		dialog = new JDialog();

		dialog.add(panel);
		dialog.pack();
		dialog.setModal(true);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
	}

	private void setUpDescriptionField() {
		JLabel descriptionLabel = new JLabel("Description");
		JLabel infoLabel = new JLabel(
				"<html><p>This will update all selected images with the entered metadata.</p><p>Leave out the fields you don't want to change.</p><br /></html>");

		descriptionField = new JTextField(16);

		panel.add(infoLabel, "wrap");
		panel.add(descriptionLabel, "wrap");
		panel.add(descriptionField, "wrap");
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
		panel.add(rater, "wrap");
	}

	private void setUpTagTextField() {
		JLabel tagLabel = new JLabel("Tags");
		tagTextField = new JTextField(16);

		panel.add(tagLabel, "wrap");
		panel.add(tagTextField, "wrap");
	}

	private void setUpAddTagButton() {
		addTagButton = new JButton("Add Tag");

		addTagButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!tagTextField.getText().isEmpty()) {
					tagText = tagTextField.getText();
					tagListModel.addElement(tagText);
					tagTextField.setText(null);
				}
			}
		});

		panel.add(addTagButton, "split 2");
	}

	private void setUpDeleteTagButton() {
		deleteTagButton = new JButton("Delete Tag");

		deleteTagButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedTagIndex = tagList.getSelectedIndex();
				if (selectedTagIndex >= 0)
					tagListModel.remove(selectedTagIndex);
			}
		});

		panel.add(deleteTagButton, "wrap");
	}

	private void setUpTagList() {
		tagList = new JList<String>(tagListModel);
		JScrollPane scrollPane = new JScrollPane(tagList);

		panel.add(scrollPane, "growx, wrap");
	}

	private void setUpSaveButton() {
		saveButton = new JButton("Save");

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ImagesUpdate imagesUpdate = createImagesUpdate();

				modifyImage.updateMultipleImages(imagesUpdate);

				dialog.setVisible(false);
			}

			private ImagesUpdate createImagesUpdate() {
				List<Long> ids = new ArrayList<>();

				for (ImageObject image : thumbPanel.getSelectedImages()) {
					ids.add(image.getId());
				}

				ImageFull imageFull = createImageFull();

				ImagesUpdate imagesUpdate = new ImagesUpdate();
				imagesUpdate.setIds(ids);
				imagesUpdate.setMetadata(imageFull);

				return imagesUpdate;
			}

			private ImageFull createImageFull() {
				String description = descriptionField.getText();

				if (description.trim().equals("")) {
					description = null;
				}

				ImageFull imageFull = new ImageFull();
				imageFull.setDescription(description);
				imageFull.setRating(getSelectedRating());

				List<String> tags = new ArrayList<>();

				for (Object tag : tagListModel.toArray()) {
					tags.add((String) tag);
				}

				imageFull.setTags(tags);
				return imageFull;
			}
		});

		panel.add(saveButton, "split 2, align right");
	}

	private void setUpCloseButton() {
		closeButton = new JButton("Close");

		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		panel.add(closeButton);
	}

	private int getSelectedRating() {
		selectedRating = 0;

		for (int i = 0; i < 5; i++) {
			if (ratingButtons[i].isSelected()) {
				selectedRating = i + 1;
			}
		}

		return selectedRating;
	}

	public void display() {
		dialog.setVisible(true);
	}
}
