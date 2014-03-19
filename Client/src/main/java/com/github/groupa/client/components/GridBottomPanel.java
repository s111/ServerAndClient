package com.github.groupa.client.components;

import java.awt.Component;

import javax.swing.JPanel;

import com.github.groupa.client.views.GridView;

public class GridBottomPanel {
	private JPanel panel;

	// static final int FPS_MIN = 0;
	// static final int FPS_MAX = 3;
	// static final int FPS_INIT = 0;
	//
	// JLabel sliderLabel = new JLabel("Thumbnail Size", JLabel.CENTER);
	//
	// JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX,
	// FPS_INIT);

	public GridBottomPanel(GridView gridView) {

		panel = new JPanel();
		new ZoomSlider().setUpZoomSlider(panel);

	}

	// public void addComponents() {
	// sliderLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
	//
	// panel.add(sliderLabel);
	// panel.add(zoomSlider);
	// }

	public Component getPanel() {
		return panel;
	}

}
