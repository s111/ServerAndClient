package com.github.groupa.client.main;

import java.awt.Dimension;

import javax.swing.JMenuBar;

import com.github.groupa.client.gui.MainFrame;
import com.github.groupa.client.gui.panels.RootPanel;

public class Main {
	private static final String APPLICATION_TITLE = "Photo Manager";

	private static final int APPLICATION_MINIMUM_WIDTH = 640;
	private static final int APPLICATION_MINIMUM_HEIGHT = 480;

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame(new RootPanel(), new JMenuBar());
		mainFrame.setTitle(APPLICATION_TITLE);
		mainFrame.setMinimumSize(new Dimension(APPLICATION_MINIMUM_WIDTH,
				APPLICATION_MINIMUM_HEIGHT));

		Application application = new Application(mainFrame);
		application.run();
	}
}
