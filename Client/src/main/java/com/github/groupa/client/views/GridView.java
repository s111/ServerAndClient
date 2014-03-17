package com.github.groupa.client.views;

import java.awt.BorderLayout;

import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.groupa.client.Library;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.components.SearchField;
import com.github.groupa.client.components.ThumbPanel;
import com.google.common.eventbus.EventBus;

public class GridView {
	private JPanel mainPanel;
	private ThumbPanel thumbPanel;
	private EventBus eventBus;
	private Library library = null;

	@Inject
	public GridView(EventBus eventBus, SingleLibrary library) {
		this.eventBus = eventBus;
		eventBus.register(this);
		setUpImageViewer();
		setLibrary(library);
	}

	public JPanel getPanel() {
		return mainPanel;
	}
	
	public void setLibrary(Library library) {
		this.library  = library;
		thumbPanel.libraryChanged();
	}
	
	public Library getLibrary() {
		return library;
	}

	private void setUpImageViewer() {
		mainPanel = new JPanel(new BorderLayout());
		thumbPanel = new ThumbPanel(eventBus, this);
		mainPanel.add(new JScrollPane(thumbPanel), BorderLayout.CENTER);
		mainPanel.add(new SearchField(eventBus, this).getPanel(), BorderLayout.NORTH);
	}

}
