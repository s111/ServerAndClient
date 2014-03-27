package com.github.groupa.client.gui;

import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.github.groupa.client.gui.panels.RootPanel;

public class MainFrame implements Frame {
	private static final String TITLE = "Photo Manager";

	private static final int MINIMUM_WIDTH = 640;
	private static final int MINIMUM_HEIGHT = 480;

	private JFrame frame = new JFrame();

	private RootPanel rootPanel;

	@Inject
	public MainFrame(JMenuBar menuBar, RootPanel rootPanel) {
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.setMinimumSize(new Dimension(MINIMUM_WIDTH,
				MINIMUM_HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(rootPanel.getPanel());

		this.rootPanel = rootPanel;
	}

	@Override
	public JFrame getFrame() {
		return frame;
	}

	public void display() {
		frame.setVisible(true);
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public void setMinimumSize(Dimension minimumSize) {
		frame.setMinimumSize(minimumSize);
	}
}
