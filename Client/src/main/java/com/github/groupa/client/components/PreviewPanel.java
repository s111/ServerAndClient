package com.github.groupa.client.components;

import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PreviewPanel {
	private JPanel picturePreviewPanel;
	private JPanel panel;
	
	private MetadataField metadataField;
	
	@Inject
	public PreviewPanel(MetadataField metadataField) {
		this.metadataField = metadataField;
		
		panel = new JPanel();
		picturePreviewPanel = new JPanel();
		
		setUpPanels();
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	private void setUpPanels() {
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(picturePreviewPanel);
		panel.add(metadataField.getPanel());
		
	}
}
