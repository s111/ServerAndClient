package com.github.groupa.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.events.UploadImageEvent;
import com.github.groupa.client.main.Main;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.View;
import com.github.groupa.client.gui.panels.ImagePanel;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class MenuBar {
	private JMenuBar menuBar = new JMenuBar();

	private EventBus eventBus;

	private JMenuItem fetchImagesItem;
	private JMenuItem uploadImageItem;
	private JMenuItem cropImage;

	private ImagePanel imagePanel;

	private JMenu edit;
	private JMenu fileMenu;

	@Inject
	public MenuBar(EventBus eventBus, ImagePanel imagePanel) {
		this.eventBus = eventBus;
		this.imagePanel = imagePanel;
		
		eventBus.register(this);

		setUpFetchImages();
		setUpUploadImage();
		setEnableCropping();

		fileMenu = new JMenu("File");
		fileMenu.add(fetchImagesItem);
		fileMenu.add(uploadImageItem);
		
		edit = new JMenu("Edit");
		edit.setEnabled(false);
		edit.add(cropImage);
		
		menuBar.add(fileMenu);
		menuBar.add(edit);
	}

	private void setUpUploadImage() {
		uploadImageItem = new JMenuItem("Upload image");
		uploadImageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				JFileChooser chooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".jpg, .jpeg, .gif, .png", "jpg", "jpeg", "gif", "png");

				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					eventBus.post(new UploadImageEvent(chooser
							.getSelectedFile()));
				}
			}
		});
	}

	private void setUpFetchImages() {
		fetchImagesItem = new JMenuItem("Fetch images");
		fetchImagesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						RESTService restService = Main.injector
								.getInstance(RESTService.class);

						// TODO We need to fix the way we import images.
						// And more importanly we need to disable this menu
						// until the thread is completed.
						new ImageListFetcher(restService).importAllImages();
					}
				}).start();
			}
		});
	}
	
	private void setEnableCropping() {
		cropImage = new JMenuItem("Enable cropping");
		cropImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				imagePanel.setSelectionEnabled(true);
			}
		});
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		edit.setEnabled(event.getView().equals(View.IMAGE_VIEW));
	}
}
