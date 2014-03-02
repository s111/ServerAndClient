package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar {
	private JMenuBar menuBar;
	private JMenuItem importItem;

	public MenuBar() {
		createMenuBar();
	}

	private void createMenuBar() {
		menuBar = new JMenuBar();
		importItem = new JMenuItem("Import image");

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(importItem);

		menuBar.add(fileMenu);
		
		importItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent action) {
				new FileChooser();
			}
		});
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}
}