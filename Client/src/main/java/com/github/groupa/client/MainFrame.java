package com.github.groupa.client;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class MainFrame {
	private JFrame frame;
	private Container contentPane;

	private CardLayout cardLayout;

	@Inject
	public MainFrame() {
		setUpMainFrame();
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public void setMenuBar(JMenuBar menuBar) {
		frame.setJMenuBar(menuBar);
	}

	private void setUpMainFrame() {
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cardLayout = new CardLayout();

		contentPane = frame.getContentPane();
		contentPane.setLayout(cardLayout);
	}

	public void display() {
		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void addView(Container content, String name) {
		contentPane.add(content, name);
	}

	public void showLastView() {
		cardLayout.previous(contentPane);
	}

	public void showView(String name) {
		cardLayout.show(contentPane, name);
	}
}