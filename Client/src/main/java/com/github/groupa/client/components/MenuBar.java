package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

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
				JFileChooser chooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".jpg, .jpeg, .gif, .png", "jpg", "jpeg", "gif", "png");

				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose to open this file: "
							+ chooser.getSelectedFile().getName());
				}
			}
		});
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}
}