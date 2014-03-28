package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.ActiveImage;
import com.github.groupa.client.ConstrainedLibrary;
import com.github.groupa.client.Library;
import com.google.common.eventbus.EventBus;

public class GridSidebarPanel implements SidebarPanel {
	private JPanel panel = new JPanel();

	private EventBus eventBus;

	private Library library;

	private GridPanel gridView;

	private ActiveImage activeImage;

	private JButton searchButton;

	private JTextField searchField;

	// TODO This should not depend on grid view, rather pass the update as a
	// event!
	@Inject
	public GridSidebarPanel(EventBus eventBus, Library library,
			GridPanel gridView, ActiveImage activeImage) {
		this.eventBus = eventBus;
		this.library = library;
		this.gridView = gridView;
		this.activeImage = activeImage;

		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		setUpSearchComponents();
		setUpSortComponents();
		setUpTagComponents();
	}

	private void setUpTagComponents() {
		panel.add(new JLabel("Tags"), "wrap");

		DefaultListModel<Object> defaultListModel = new DefaultListModel<>();
		defaultListModel.addElement("tag1");
		defaultListModel.addElement("tag2");
		defaultListModel.addElement("tag3");

		JScrollPane scrollPane = new JScrollPane(new JList<>(defaultListModel));

		panel.add(scrollPane, "grow, push");
	}

	private void setUpSortComponents() {
		panel.add(new JLabel("Sort"), "wrap");

		DefaultComboBoxModel<Object> defaultComboBoxModel = new DefaultComboBoxModel<>();
		defaultComboBoxModel.addElement("test1");
		defaultComboBoxModel.addElement("test2");

		panel.add(new JComboBox<>(defaultComboBoxModel), "width 128, wrap");
	}

	private void setUpSearchComponents() {
		panel.add(new JLabel("Search"), "wrap");

		searchField = new JTextField();

		panel.add(searchField, "split 2, width 128");
		searchButton = new JButton("Search");
		panel.add(searchButton, "wrap");

		addSearchListeners();
	}

	private void addSearchListeners() {
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String text = searchField.getText();
				Library lib;
				if (text == null || text.equals("")) {
					lib = library;
				} else {
					lib = new ConstrainedLibrary(eventBus, library)
							.addConstraint(ConstrainedLibrary.HAS_TAG, text);
					eventBus.register(lib);
				}

				if (gridView != null) {
					activeImage.setActiveLibrary(lib);

					gridView.setLibrary(lib);
				}
			}
		};

		searchButton.addActionListener(actionListener);
		searchField.addActionListener(actionListener);
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
