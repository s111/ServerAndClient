package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.groupa.client.Library;
import com.github.groupa.client.Main;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.views.GridView;
import com.github.groupa.client.views.ImageView;

public class SearchField implements ActionListener {
	private JPanel panel;
	private JTextField searchField;
	private JButton searchButton;
	private ImageView imageView = null;
	private GridView gridView = null;
	private Library mainLibrary;
	
	public SearchField(ImageView imageView) {
		this.imageView = imageView;
		mainLibrary = Main.injector.getInstance(SingleLibrary.class);
		setUpPanels();
	}

	public SearchField(GridView gridView) {
		this.gridView = gridView;
		mainLibrary = Main.injector.getInstance(SingleLibrary.class);
		setUpPanels();
	}
	
	private void setUpPanels() {
		panel = new JPanel();

		searchField = new JTextField(8);
		searchButton = new JButton("Search");
		
		searchButton.addActionListener(this);

		panel.add(searchField);
		panel.add(searchButton);
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = searchField.getText();
		if (text == null || text == "") JOptionPane.showInputDialog("Nothing was entered!");
		
		
		
	}
}
