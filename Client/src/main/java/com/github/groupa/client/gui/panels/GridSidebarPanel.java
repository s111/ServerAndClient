package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.components.ZoomSlider;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.library.LibraryConstraint;
import com.github.groupa.client.library.LibrarySort;
import com.github.groupa.client.library.RatingConstraint;
import com.github.groupa.client.library.TagConstraint;

public class GridSidebarPanel implements SidebarPanel {
	private JPanel panel = new JPanel();

	private ThumbPanel thumbPanel;
	private Library library;
	private ZoomSlider zoomSlider;

	@Inject
	public GridSidebarPanel(Library rootLibrary, ThumbPanel thumbPanel,
			ZoomSlider zoomSlider) {
		this.thumbPanel = thumbPanel;
		this.zoomSlider = zoomSlider;
		library = new Library(rootLibrary);
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		setUpConstraintComponents();
		setUpSortComponents();
		setUpTagComponents();
		setUpZoomComponents();
		setUpEditMetadataComponent();

	}

	private void setUpZoomComponents() {
		panel.add(zoomSlider.getPanel(), "align center, wrap");
	}

	private void setUpTagComponents() {
		panel.add(new JLabel("Tags"), "wrap");

		DefaultListModel<Object> defaultListModel = new DefaultListModel<>();
		defaultListModel.addElement("tag1");
		defaultListModel.addElement("tag2");
		defaultListModel.addElement("tag3");

		JScrollPane scrollPane = new JScrollPane(new JList<>(defaultListModel));

		panel.add(scrollPane, "grow, push, wrap");
	}

	private void setUpConstraintComponents() {
		String[] sortTypes = { "Filters", "Add tag filter", "Add rating filter" };

		final List<LibraryConstraint> constraints = new ArrayList<>();
		final JComboBox<String> comboBox = new JComboBox<>(sortTypes);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						constraintAction(comboBox, constraints);
					}
				});
			}
		});
		panel.add(comboBox, "width 128, wrap");
	}

	private void constraintAction(JComboBox<String> comboBox, List<LibraryConstraint> constraints) {
		Object item = comboBox.getSelectedItem();
		if (item instanceof String) {
			LibraryConstraint constraint = null;
			if ("Add tag filter".equals(item)) {
				constraint = TagConstraint.create();
			} else if ("Add rating filter".equals(item)) {
				constraint = RatingConstraint.create();
			} else {
				for (LibraryConstraint c : constraints) {
					if (item.equals(c.toString())) {
						comboBox.removeItem(item);
						thumbPanel.setLibrary(library.removeConstraint(c));
						constraints.remove(c);
						comboBox.setSelectedIndex(0);
						break;
					}
				}
			}
			if (constraint != null) {
				for (LibraryConstraint c : constraints) {
					if (item.equals(c.toString())) {
						comboBox.setSelectedIndex(0);
						return;
					}
				}
				constraints.add(constraint);
				comboBox.addItem(constraint.toString());
				thumbPanel.setLibrary(library.addConstraint(constraint));
				comboBox.setSelectedIndex(0);
			}
		}
	}

	private void setUpSortComponents() {
		panel.add(new JLabel("Sort"), "wrap");
		String[] sortTypes = { "ID", "Rating (desc)", "Rating (asc)" };

		final JComboBox<String> comboBox = new JComboBox<>(sortTypes);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (comboBox.getSelectedIndex() == 0) {
							thumbPanel.sort(LibrarySort.SORT_ID_ASC);
						} else if (comboBox.getSelectedIndex() == 1) {
							thumbPanel.sort(LibrarySort.SORT_RATING_DESC);
						} else if (comboBox.getSelectedIndex() == 2) {
							thumbPanel.sort(LibrarySort.SORT_RATING_ASC);
						}
					}
				});
			}
		});
		panel.add(comboBox, "width 128, wrap");
	}

	private void setUpEditMetadataComponent() {
		JButton editMetadataButton;

		editMetadataButton = new JButton("Edit Metadata");

		editMetadataButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				new EditMetadataWindow();
			}
		});

		panel.add(editMetadataButton);
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
