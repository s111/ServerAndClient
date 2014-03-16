package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.events.UploadImageEvent;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;

public class MenuBar {
	private JMenuBar menuBar;
	private JMenuItem importItem, imageViewItem;

	private EventBus eventBus;

	@Inject
	public MenuBar(EventBus eventBus) {
		this.eventBus = eventBus;
		
		setUpMenuBar();
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	private void setUpMenuBar() {
		menuBar = new JMenuBar();

		importItem = new JMenuItem("Import image");
		imageViewItem = new JMenuItem("Activate ImageView");

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(importItem);
		fileMenu.add(imageViewItem);

		menuBar.add(fileMenu);

		addActionListeners();

	}

	private void addActionListeners() {
		importItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				JFileChooser chooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".jpg, .jpeg, .gif, .png", "jpg", "jpeg", "gif", "png");

				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					eventBus.post(new UploadImageEvent(chooser.getSelectedFile()));
				}
			}
		});

		imageViewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW));
			}
		});
	}
}