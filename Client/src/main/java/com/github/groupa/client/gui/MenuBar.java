package com.github.groupa.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.main.Main;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;

public class MenuBar {
	private JMenuBar menuBar = new JMenuBar();

	private EventBus eventBus;

	private JMenuItem fetchImagesItem;
	private JMenuItem showGridView;
	private JMenuItem showImageView;

	@Inject
	public MenuBar(EventBus eventBus) {
		this.eventBus = eventBus;

		setUpFetchImages();
		setUpShowImageView();
		setUpShowGridView();

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(fetchImagesItem);
		fileMenu.add(showGridView);
		fileMenu.add(showImageView);

		menuBar.add(fileMenu);
	}

	private void setUpShowGridView() {
		showGridView = new JMenuItem("Show grid view");
		showGridView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				eventBus.post(new SwitchViewEvent(View.GRID_VIEW));
			}
		});
	}

	private void setUpShowImageView() {
		showImageView = new JMenuItem("Show image view");
		showImageView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW));
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

	public JMenuBar getMenuBar() {
		return menuBar;
	}
}
