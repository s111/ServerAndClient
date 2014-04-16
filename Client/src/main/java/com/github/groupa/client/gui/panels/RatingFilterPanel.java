package com.github.groupa.client.gui.panels;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;

public class RatingFilterPanel {
	private JPanel panel = new JPanel();

	private int currentMinLevel = 0;
	private int currentMaxLevel = 5;

	private JSlider minSlider;

	private JSlider maxSlider;

	@Inject
	public RatingFilterPanel() {
		panel.setLayout(new MigLayout());

		setUpRatingSlider();
	}

	private void setUpRatingSlider() {
		minSlider = new JSlider(JSlider.HORIZONTAL, 0, 5, currentMinLevel);
		maxSlider = new JSlider(JSlider.HORIZONTAL, 0, 5, currentMaxLevel);
		final Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		table.put(0, new JLabel("0"));
		table.put(1, new JLabel("1"));
		table.put(2, new JLabel("2"));
		table.put(3, new JLabel("3"));
		table.put(4, new JLabel("4"));
		table.put(5, new JLabel("5"));

		minSlider.setLabelTable(table);
		minSlider.setPaintLabels(true);
		minSlider.setSnapToTicks(true);
		minSlider.setMajorTickSpacing(1);
		minSlider.setMinorTickSpacing(1);

		maxSlider.setLabelTable(table);
		maxSlider.setPaintLabels(true);
		maxSlider.setSnapToTicks(true);
		maxSlider.setMajorTickSpacing(1);
		maxSlider.setMinorTickSpacing(1);

		panel.add(minSlider, "wrap");
		panel.add(maxSlider);

		minSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent Event) {
				currentMinLevel = minSlider.getValue();

				if (!minSlider.getValueIsAdjusting()) {
					if (currentMinLevel > currentMaxLevel) {
						minSlider.setValue(currentMaxLevel);
					}
				}
			}
		});

		maxSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent Event) {
				currentMaxLevel = maxSlider.getValue();

				if (!maxSlider.getValueIsAdjusting()) {
					if (currentMaxLevel < currentMinLevel) {
						maxSlider.setValue(currentMinLevel);
					}
				}
			}
		});
	}

	public JPanel getPanel() {
		return panel;
	}
}
