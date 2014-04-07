package com.github.groupa.client.gui.panels;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.inject.Inject;

public class ZoomSlider implements Panel {
	private int currentLevel = 1;
	private ThumbPanel thumbPanel;
	private JPanel panel = new JPanel();

	@Inject
	public ZoomSlider(ThumbPanel thumbPanel) {
		this.thumbPanel = thumbPanel;

		setUpZoomSlider();
	}

	private void setUpZoomSlider() {
		JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 0, 3, currentLevel);
		final Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		table.put(0, new JLabel("s"));
		table.put(1, new JLabel("m"));
		table.put(2, new JLabel("l"));
		table.put(3, new JLabel("xl"));
		zoomSlider.setLabelTable(table);
		zoomSlider.setPaintLabels(true);
		zoomSlider.setSnapToTicks(true);
		zoomSlider.setMajorTickSpacing(1);
		zoomSlider.setMinorTickSpacing(1);
		panel.add(zoomSlider);

		zoomSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JSlider source = (JSlider) arg0.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = source.getValue();
					if (value != currentLevel) {
						currentLevel = value;
						thumbPanel
								.setPanelThumbSize(table.get(value).getText());
					}
				}
			}
		});
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
