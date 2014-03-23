package com.github.groupa.client.components;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.github.groupa.client.views.GridView;

public class previewPanel {

	private GridView gridView;
	private JPanel picturePreviewPanel;
	private JPanel panel;
	
	public previewPanel() {
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
		panel.add(new MetadataField(gridView).getPanel());
		
	}
}
