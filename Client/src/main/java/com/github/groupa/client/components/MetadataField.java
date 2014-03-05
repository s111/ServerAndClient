package com.github.groupa.client.components;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MetadataField {
	private final int FILLER_HEIGHT = 15;
	private final int FILLER_WIDTH = 10;
	
	private JPanel panel;
	
	private JLabel descriptionLabel = new JLabel("abc");
	private JLabel tagsLabel = new JLabel("tag1, tag2, tag3");
	private JLabel ratingLabel = new JLabel("5");

	public MetadataField() {
		panel = new JPanel();

		setUpPanels();
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void setDescription(String description) {
		descriptionLabel.setText(description);
	}
	
	public void setTags(String tags) {
		tagsLabel.setText(tags);
	}
	
	public void setRating(Integer rating) {
		ratingLabel.setText(rating.toString());
	}

	private void setUpPanels() {
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(createHeaderLabel("Description"));
		panel.add(descriptionLabel);

		createFiller();

		panel.add(createHeaderLabel("Tag"));
		panel.add(tagsLabel);

		createFiller();

		panel.add(createHeaderLabel("Rating"));
		panel.add(ratingLabel);

		createFiller();
	}

	private JLabel createHeaderLabel(String headerName) {
		Font f = panel.getFont();

		JLabel headerLabel = new JLabel(headerName + ": ");
		headerLabel.setFont(f.deriveFont(Font.BOLD));

		return headerLabel;
	}

	private void createFiller() {
		Dimension filler = new Dimension(FILLER_WIDTH, FILLER_HEIGHT);
		panel.add(Box.createRigidArea(filler));
	}
}