package com.github.groupa.client.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.gui.panels.IRootPanel;
import com.github.groupa.client.servercommunication.RESTService;

public class MenuBar {
	private JMenuBar menuBar = new JMenuBar();

	private IRootPanel rootPanel;

	private JMenuItem fetchImagesItem;

	@Inject
	public MenuBar(IRootPanel rootPanel) {
		this.rootPanel = rootPanel;

		setUpFetchImages();

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(fetchImagesItem);

		menuBar.add(fileMenu);
	}

	private void setUpFetchImages() {
		fetchImagesItem = new JMenuItem("Fetch images");
		fetchImagesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
