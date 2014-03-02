package com.github.groupa.client;

import javax.swing.*;

import com.github.groupa.client.components.ImageRater;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

public class ImageViewer {
	private JFrame frame;

	private Container contentPane;

	private String title;

	public ImageViewer(String title) {
		this.title = title;

		setUpImageViewer();
	}

	public void display() {
		frame.setVisible(true);
	}

	private void setUpImageViewer() {
		frame = new JFrame(title);
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		addPanelsToContentPane(contentPane);

		frame.pack();
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		JLabel label = new JLabel("Top Toolbar");

		topPanel.add(label);

		return topPanel;
	}

	private JPanel createPicturePanel() {
		JPanel picturePanel = new JPanel();
		picturePanel.setBackground(Color.GREEN);

		return picturePanel;
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel();
		JLabel label = new JLabel("Left Toolbar");

		leftPanel.add(label);

		return leftPanel;
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		
		bottomPanel.add(new ImageRater().getPanel());

		return bottomPanel;
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel();
		JLabel label = new JLabel("Right Toolbar");

		rightPanel.add(label);

		return rightPanel;
	}

	private void addPanelsToContentPane(Container contentPane) {
		JPanel topPanel = createTopPanel();
		JPanel picturePanel = createPicturePanel();
		JPanel leftPanel = createLeftPanel();
		JPanel bottomPanel = createBottomPanel();
		JPanel rightPanel = createRightPanel();

		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(picturePanel, BorderLayout.CENTER);
		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		contentPane.add(rightPanel, BorderLayout.EAST);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ImageViewer imageViewer = new ImageViewer("Title");
				imageViewer.display();
			}
		});
	}
}
