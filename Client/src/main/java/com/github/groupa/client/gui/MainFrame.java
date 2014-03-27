package com.github.groupa.client.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.github.groupa.client.gui.panels.RootPanel;

public class MainFrame implements Frame {
	private JFrame frame = new JFrame();

	private RootPanel rootPanel;

	public MainFrame(RootPanel rootPanel) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		this.rootPanel = rootPanel;

		frame.add(rootPanel.getPanel());
	}

	public void display() {
		frame.setVisible(true);
	}

	@Override
	public JFrame getFrame() {
		return frame;
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public void setMinimumSize(Dimension minimumSize) {
		frame.setMinimumSize(minimumSize);
	}
}
