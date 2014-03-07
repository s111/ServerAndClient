package com.github.groupa.client;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.github.groupa.client.components.MenuBar;

public class MainFrame {
	private JFrame frame;
	private Container contentPane;

	private String title;

	public MainFrame(String title) {
		this.title = title;

		setUpMainFrame();
	}

	private void setUpMainFrame() {
		frame = new JFrame(title);
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setJMenuBar(new MenuBar().getMenuBar());
		
		contentPane = frame.getContentPane();
	}

	public void display() {
		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void replaceContent(Container content) {
		contentPane.removeAll();
		contentPane.add(content);
	}
}