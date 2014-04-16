package com.github.groupa.client.gui.panels;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;

public class RatingFilterPanel {
	private JPanel panel = new JPanel();

	private int currentLevel = 0;

	@Inject
	public RatingFilterPanel() {
		panel.setLayout(new MigLayout());

		setUpRatingSlider();
	}

	private void setUpRatingSlider() {
		JSlider minSlider = new JSlider(JSlider.HORIZONTAL, 0, 5, currentLevel);
		JSlider maxSlider = new JSlider(JSlider.HORIZONTAL, 0, 5, currentLevel);
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
	}

	public JPanel getPanel() {
		return panel;
	}
}
