package com.github.groupa.client.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.groupa.client.Library;
import com.github.groupa.client.MainFrame;

public class GridView {
	private Library library;
	private MainFrame mainFrame;

	private JPanel mainPanel;
	private JButton testButton;
	
	public GridView(Library library, MainFrame mainFrame) {
		this.library = library;
		this.mainFrame = mainFrame;
		setUpImageViewer();
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
	private void setUpImageViewer() {
		mainPanel = new JPanel();
		//mainPanel.setLayout(new BorderLayout());
		
		testButton = new JButton("ImageView");
		addButtonActionListeners();
		mainPanel.add(testButton);
	}
	
	private void addButtonActionListeners() {
		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				mainFrame.setNewView(new ImageView(library, mainFrame).getPanel());
			}
		});
	}
}
