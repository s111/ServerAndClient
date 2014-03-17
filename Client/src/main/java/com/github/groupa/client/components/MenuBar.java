package com.github.groupa.client.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.Library;
import com.github.groupa.client.Main;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.events.UploadImageEvent;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;

public class MenuBar {
	private JMenuBar menuBar;
	private JMenuItem importItem, imageViewItem, getAllImages;

	private EventBus eventBus;
	private ImageListFetcher imageListFetcher;

	@Inject
	public MenuBar(EventBus eventBus, ImageListFetcher imageListFetcher) {
		this.eventBus = eventBus;
		this.imageListFetcher = imageListFetcher;
		setUpMenuBar();
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	private void setUpMenuBar() {
		menuBar = new JMenuBar();

		importItem = new JMenuItem("Import image");
		imageViewItem = new JMenuItem("Activate ImageView");
		getAllImages = new JMenuItem("Fetch all images");

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(importItem);
		fileMenu.add(imageViewItem);
		fileMenu.add(getAllImages);

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
		
		getAllImages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						Library lib = imageListFetcher.importAllImages();
						if (lib != null) {
							Main.injector.getInstance(SingleLibrary.class).addAll(lib);
						}
					}
				});
			}
		});
	}
}