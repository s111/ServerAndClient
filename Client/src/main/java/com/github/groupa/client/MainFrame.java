package com.github.groupa.client;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame {
	private static final Logger logger = LoggerFactory
			.getLogger(MainFrame.class);

	private JFrame frame;
	private Container contentPane;
	private Container displayedContainer;

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

		contentPane = frame.getContentPane();
	}

	public void display() {
		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setImageView() {
		contentPane.removeAll();
		ExampleImageViewer imageViewer = new ExampleImageViewer();
		displayedContainer = imageViewer.getPanel();
		contentPane.add(displayedContainer);
	}
}