package com.github.groupa.client.components;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MetadataField {
	private String descriptionText = "very description";
	private JPanel panel;
	private JLabel descriptionLabel;
	private JLabel descriptionContent;

	public MetadataField() {
		panel = new JPanel();
		createDescriptionLabels();
		
		
		setUpPanels();
	}
	
	private void setUpPanels() {
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(descriptionLabel);
		panel.add(descriptionContent);
	}

	public JPanel getPanel() {
		return panel;
	}

	private void createDescriptionLabels() {
		Font f = panel.getFont();

		descriptionLabel = new JLabel("Description: ");
		descriptionLabel.setFont(f.deriveFont(Font.BOLD));

		descriptionContent = new JLabel(descriptionText);

	}
}
