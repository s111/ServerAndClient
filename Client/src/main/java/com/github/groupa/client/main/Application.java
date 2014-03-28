package com.github.groupa.client.main;

import javax.inject.Inject;
import javax.swing.SwingUtilities;

import com.github.groupa.client.gui.frames.MainFrame;

public class Application {
	public static final String BASEURL = "http://localhost:9000/api";

	private MainFrame mainFrame;

	@Inject
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
