package com.github.groupa.client.gui;

import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.github.groupa.client.gui.panels.ContentPanel;
import com.github.groupa.client.gui.panels.GridContentPanel;
import com.github.groupa.client.gui.panels.GridSidebarPanel;
import com.github.groupa.client.gui.panels.RootPanel;
import com.github.groupa.client.gui.panels.SidebarPanel;

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
		frame.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(rootPanel.getPanel());

		this.rootPanel = rootPanel;

		setUpRootPanel();
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

	private void setUpRootPanel() {
		SidebarPanel gridSidebarPanel = new GridSidebarPanel();
		ContentPanel gridContentPanel = new GridContentPanel();

		rootPanel.addSidebarPanel("gridSidebarPanel", gridSidebarPanel);
		rootPanel.addContentPanel("gridContentPanel", gridContentPanel);
	}
}
