package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.groupa.client.ConstrainedLibrary;
import com.github.groupa.client.Library;
import com.github.groupa.client.views.GridView;
import com.github.groupa.client.views.ImageView;
import com.google.common.eventbus.EventBus;

public class SearchField implements ActionListener {
	private JPanel panel;
	private JTextField searchField;
	private JButton searchButton;
	private GridView gridView = null;
	private EventBus eventBus;
	private Library mainLibrary;

	public SearchField(EventBus eventBus, Library mainLibrary, ImageView imageView) {
		this.eventBus = eventBus;
		this.mainLibrary = mainLibrary;
		setUpPanels();
	}

	public SearchField(EventBus eventBus, Library mainLibrary, GridView gridView) {
		this.eventBus = eventBus;
		this.gridView = gridView;
		this.mainLibrary = mainLibrary;

		setUpPanels();
	}

	private void setUpPanels() {
		panel = new JPanel();

		searchField = new JTextField(8);
		searchButton = new JButton("Search");

		searchButton.addActionListener(this);
		searchField.addActionListener(this);

		panel.add(searchField);
		panel.add(searchButton);
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = searchField.getText();
		Library lib;
		if (text == null || text.equals("")) {
			lib = mainLibrary;
		} else {
			lib = new ConstrainedLibrary(mainLibrary).addConstraint(
					ConstrainedLibrary.HAS_TAG, text);
			eventBus.register(lib);
		}

		if (gridView != null)
			gridView.setLibrary(lib);
	}

}
