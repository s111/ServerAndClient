package com.github.groupa.client;

import java.awt.Container;
import java.awt.Dimension;
import java.util.Stack;

import javax.swing.JFrame;

import com.github.groupa.client.components.MenuBar;

public class MainFrame {
	private JFrame frame;
	private Container contentPane;

	private String title;

	private Stack<Container> views = new Stack<>();

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

	public void setNewView(Container content) {
		views.push(content);
		setView();
	}

	public void setLastView() {
		if (hasPreviousView()) {
			views.pop();
			setView();
		}
	}

	public boolean hasPreviousView() {
		return views.size() > 1;
	}

	private void setView() {
		contentPane.removeAll();
		contentPane.add(views.peek());
		frame.pack();
	}
}