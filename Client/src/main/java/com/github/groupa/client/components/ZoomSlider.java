package com.github.groupa.client.components;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ZoomSlider {

	static final int THUMBSIZE_MIN = 0;
	static final int THUMBSIZE_MAX = 2;
	static final int THUMBSIZE_INIT = 0;

	JLabel sliderLabel = new JLabel("Thumbnail Size", JLabel.CENTER);

	JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, THUMBSIZE_MIN,
			THUMBSIZE_MAX, THUMBSIZE_INIT);

	public void setUpZoomSlider(JPanel panel) {
		sliderLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		zoomSlider.setMinorTickSpacing(1);
		zoomSlider.setPaintTicks(true);

		addComponents(panel);
	}

	public void addComponents(JPanel panel) {
		panel.add(sliderLabel);
		panel.add(zoomSlider);
	}

}
