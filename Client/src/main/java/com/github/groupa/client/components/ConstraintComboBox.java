package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import com.github.groupa.client.library.Library;
import com.github.groupa.client.library.LibraryConstraint;
import com.github.groupa.client.library.RatingConstraint;
import com.github.groupa.client.library.TagConstraint;
import com.google.inject.Inject;

@SuppressWarnings("serial")
public class ConstraintComboBox extends JComboBox<String> {
	private static String[] initialItems = { "Filters", "Add tag filter", "Add rating filter" };

	private Library library;
	private List<LibraryConstraint> constraints = new ArrayList<>();

	@Inject
	public ConstraintComboBox(Library library) {
		super(initialItems);
		this.library = library;
		setUpConstraintComponents();
	}
	
	private void setUpConstraintComponents() {
		addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						constraintAction();
					}
				});
			}
		});
	}

	private void constraintAction() {
		Object item = getSelectedItem();
		if (item instanceof String) {
			LibraryConstraint constraint = null;
			if ("Add tag filter".equals(item)) {
				constraint = TagConstraint.create();
			} else if ("Add rating filter".equals(item)) {
				constraint = RatingConstraint.create();
			} else {
				for (LibraryConstraint c : constraints) {
					if (item.equals(c.toString())) {
						removeItem(item);
						library.removeConstraint(c);
						constraints.remove(c);
						setSelectedIndex(0);
						break;
					}
				}
			}
			if (constraint != null) {
				for (LibraryConstraint c : constraints) {
					if (item.equals(c.toString())) {
						setSelectedIndex(0);
						return;
					}
				}
				constraints.add(constraint);
				addItem(constraint.toString());
				library.addConstraint(constraint);
				setSelectedIndex(0);
			}
		}
	}
}
