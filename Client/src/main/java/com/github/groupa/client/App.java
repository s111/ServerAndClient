package com.github.groupa.client;

import javax.swing.SwingUtilities;

public class App {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame mainFrame = new MainFrame("App");

				mainFrame.display();
			}
		});
	}
}
