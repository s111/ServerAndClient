package com.github.groupa.client.views;
import java.awt.BorderLayout;

import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.groupa.client.Library;
import com.github.groupa.client.MainFrame;
import com.github.groupa.client.components.SearchField;
import com.github.groupa.client.components.ThumbPanel;

public class GridView {
	private Library library;
	private ImageView imageView;

	private JPanel mainPanel;
	private JPanel thumbPanel;
	
	private MainFrame mainFrame;

	@Inject
	public GridView(MainFrame mainFrame, Library library, ImageView imageView) {
		this.mainFrame = mainFrame;
		this.library = library;
		this.imageView = imageView;
		setUpImageViewer();
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	private void setUpImageViewer() {
		mainPanel = new JPanel(new BorderLayout());
		thumbPanel = new ThumbPanel(library, imageView, mainFrame);
		mainPanel.add(new JScrollPane(thumbPanel), BorderLayout.CENTER);
		mainPanel.add(new SearchField(library).getPanel(), BorderLayout.NORTH);
	}
}
