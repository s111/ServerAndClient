package com.github.groupa.client.components;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuBar {
	
	public MenuBar() {
		createMenuBar();
	}
	
	private JMenuBar menuBar;
	
	private void createMenuBar() {
		menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		menuBar.add(fileMenu);
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
	private void addItemsToMenuBar() {
		
	}
}
