package com.github.groupa.client.components;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import com.github.groupa.client.Main;
import com.github.groupa.client.views.GridView;

public class GridBottomPanel {

	JPanel gridBottomPanel = new JPanel(new BorderLayout());
	JPanel rightPanel = new JPanel();
	JPanel leftPanel = new JPanel();

	public GridBottomPanel(GridView gridView) {
		setupRightPanel();
		setupLeftPanel();
		addPanels();
	}

	public void setupRightPanel() {
		new ZoomSlider().setUpZoomSlider(rightPanel);
	}

	public void setupLeftPanel() {
		leftPanel.add(Main.injector.getInstance(PreviewPanel.class).getPanel());
	}

	public void addPanels() {
		gridBottomPanel.add(rightPanel, BorderLayout.EAST);
		gridBottomPanel.add(leftPanel, BorderLayout.WEST);
	}

	public Component getPanel() {
		return gridBottomPanel;
	}

}
