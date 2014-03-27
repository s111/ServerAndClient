package com.github.groupa.client.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.Library;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.gui.panels.RootPanel;
import com.github.groupa.client.servercommunication.RESTService;

public class MenuBar {
	private JMenuBar menuBar = new JMenuBar();

	private RootPanel rootPanel;

	private JMenuItem fetchImagesItem;

	@Inject
	public MenuBar(RootPanel rootPanel) {
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
				RESTService restService = Main.injector
						.getInstance(RESTService.class);

				Library lib = new ImageListFetcher(restService)
						.importAllImages();

				if (lib != null) {
					Main.injector.getInstance(SingleLibrary.class).addAll(lib);
				}
			}
		});
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}
}
