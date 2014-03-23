package com.github.groupa.client.components;

import java.awt.Component;

import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.groupa.client.events.ImageZoomEvent;
import com.google.common.eventbus.EventBus;

public class ImageZoomSlider {

	private static final int ZOOM_MIN = 0;
	private static final int ZOOM_MAX = 9;
	private static final int ZOOM_INIT = 0;

	private JLabel sliderLabel = new JLabel("Zoom", JLabel.CENTER);

	private JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, ZOOM_MIN,
			ZOOM_MAX, ZOOM_INIT);

	private EventBus eventBus;

	@Inject
	public ImageZoomSlider(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setUpZoomSlider(JPanel panel) {
		sliderLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		zoomSlider.setMinimum(5);
		zoomSlider.setMaximum(20);
		zoomSlider.setValue(10);
		zoomSlider.setMinorTickSpacing(1);
		zoomSlider.setPaintTicks(true);

		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				float zoom = (float) zoomSlider.getValue() / 10;

				eventBus.post(new ImageZoomEvent(zoom));
			}
		});

		addComponents(panel);
	}

	public void addComponents(JPanel panel) {
		panel.add(sliderLabel);
		panel.add(zoomSlider);
	}

}
