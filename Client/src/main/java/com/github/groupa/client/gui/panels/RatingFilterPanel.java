package com.github.groupa.client.gui.panels;

import java.awt.Font;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.library.Library;
import com.github.groupa.client.library.LibraryConstraint;
import com.github.groupa.client.library.RatingConstraint;
import com.google.inject.Inject;

public class RatingFilterPanel {
	private JPanel panel = new JPanel();

	private int currentMinLevel = 0;
	private int currentMaxLevel = 5;

	private JSlider minSlider;

	private JSlider maxSlider;

	private Library library;

	@Inject
	public RatingFilterPanel(Library library) {
		this.library = library;
		panel.setLayout(new MigLayout());

		setUpRatingSlider();
	}

	private void setConstraint() {
		int min = minSlider.getValue();
		int max = maxSlider.getValue();
		for (LibraryConstraint c : library.getConstraints()) {
			if (c instanceof RatingConstraint) {
				library.removeConstraint(c);
			}
		}

		RatingConstraint rating = new RatingConstraint(min, max);
		library.addConstraint(rating);
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

		JLabel filterByRatingLabel = new JLabel("Filter by rating");
		filterByRatingLabel.setFont(new Font(filterByRatingLabel.getFont()
				.getName(), Font.BOLD, 13));

		JLabel maxRatingLabel = new JLabel("Max rating:");

		JLabel minRatingLabel = new JLabel("Min rating:");

		panel.add(filterByRatingLabel, "wrap");
		panel.add(minRatingLabel, "split 2");
		panel.add(minSlider, "wrap");
		panel.add(maxRatingLabel, "split 2");
		panel.add(maxSlider);

		minSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent Event) {
				currentMinLevel = minSlider.getValue();

				if (!minSlider.getValueIsAdjusting()) {
					setConstraint();
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
					setConstraint();
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
