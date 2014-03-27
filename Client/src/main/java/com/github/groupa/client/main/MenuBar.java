package com.github.groupa.client.main;

import javax.inject.Inject;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.github.groupa.client.gui.panels.RootPanel;

public class MenuBar {
	private JMenuBar menuBar = new JMenuBar();

	private RootPanel rootPanel;

	@Inject
	public MenuBar(RootPanel rootPanel) {
		this.rootPanel = rootPanel;

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem("Import"));

		menuBar.add(fileMenu);
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}
}
