package com.github.groupa.client.main;

import javax.swing.SwingUtilities;

import com.github.groupa.client.gui.MainFrame;

public class Application {
	private MainFrame mainFrame;

	public Application(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.display();
			}
		});
	}
}
