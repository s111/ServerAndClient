package com.github.groupa.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.inject.Inject;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.ImageUploader;
import com.github.groupa.client.ThreadPool;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.gui.panels.ImagePanel;
import com.github.groupa.client.main.Main;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class MenuBar {
	private JMenuBar menuBar = new JMenuBar();

	private ThreadPool threadPool;
	private ImageUploader imageUploader;

	private JMenuItem fetchImagesItem = new JMenuItem("Fetch images");
	private JMenuItem uploadImageItem = new JMenuItem("Upload images");
	private JMenuItem cropImage = new JMenuItem("Toggle cropping");
	private JMenuItem crop = new JMenuItem("Crop Image");

	private ImagePanel imagePanel;

	private JMenu edit;
	private JMenu fileMenu;

	@Inject
	public MenuBar(EventBus eventBus, ImagePanel imagePanel,
			ThreadPool threadPool, ImageUploader imageUploader) {
		this.imagePanel = imagePanel;
		this.threadPool = threadPool;
		this.imageUploader = imageUploader;

		eventBus.register(this);

		setUpFetchImages();
		setUpUploadImage();
		setUpToggleCropping();
		setUpCrop();

		fileMenu = new JMenu("File");
		fileMenu.add(fetchImagesItem);
		fileMenu.add(uploadImageItem);

		edit = new JMenu("Edit");
		edit.setEnabled(false);
		edit.add(cropImage);
		edit.add(crop);

		menuBar.add(fileMenu);
		menuBar.add(edit);
	}

	private void setUpUploadImage() {
		uploadImageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				JFileChooser chooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".jpg, .jpeg, .gif, .png", "jpg", "jpeg", "gif", "png");

				chooser.setFileFilter(filter);
				chooser.setMultiSelectionEnabled(true);

				int returnVal = chooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File[] files = chooser.getSelectedFiles();
					if (files.length > 0) {
						threadPool.add(new Runnable() {
							public void run() {
								imageUploader.uploadImages(files);
							}
						});
					}
				}
			}
		});
	}

	private void setUpFetchImages() {
		fetchImagesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				threadPool.add(new Runnable() {
					@Override
					public void run() {
						RESTService restService = Main.injector
								.getInstance(RESTService.class);

						// TODO We need to fix the way we import images.
						// And more importanly we need to disable this menu
						// until the thread is completed.
						new ImageListFetcher(restService).importAllImages();
					}
				});
			}
		});
	}

	private void setUpToggleCropping() {
		cropImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				imagePanel.toggleSelection();
			}
		});
	}

	private void setUpCrop() {
		crop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				imagePanel.crop();
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
