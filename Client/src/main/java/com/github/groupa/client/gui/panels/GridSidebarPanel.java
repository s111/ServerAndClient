package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import library.Library;
import library.LibrarySort;
import library.TagConstraint;
import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.components.ZoomSlider;

public class GridSidebarPanel implements SidebarPanel {
	private JPanel panel = new JPanel();

	private ThumbPanel thumbPanel;
	private Library rootLibrary;
	private ZoomSlider zoomSlider;

	@Inject
	public GridSidebarPanel(Library rootLibrary, ThumbPanel thumbPanel,
			ZoomSlider zoomSlider) {
		this.rootLibrary = rootLibrary;
		this.thumbPanel = thumbPanel;
		this.zoomSlider = zoomSlider;

		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		setUpSearchComponents();
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

	private void setUpSearchComponents() {
		panel.add(new JLabel("Search"), "wrap");

		JButton searchButton;

		JTextField searchField;
		searchField = new JTextField();

		panel.add(searchField, "split 2, width 128");
		searchButton = new JButton("Search");
		panel.add(searchButton, "wrap");

		ActionListener actionListener = getSearchListener(searchField);
		searchButton.addActionListener(actionListener);
		searchField.addActionListener(actionListener);
	}

	private ActionListener getSearchListener(final JTextField searchField) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				final String text = searchField.getText();
				if (text == null || text.equals("")) {
					thumbPanel.setLibrary(rootLibrary);
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							thumbPanel.setLibrary(new Library(rootLibrary)
									.addConstraint(new TagConstraint(text)));
						}
					});
				}
			}
		};
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
