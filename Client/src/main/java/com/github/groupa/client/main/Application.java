package com.github.groupa.client.main;

import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.ImageUploader;
import com.github.groupa.client.gui.frames.MainFrame;
import com.google.common.eventbus.EventBus;

public class Application {
	public static String BASEURL = "http://localhost:9000/api";

	private MainFrame mainFrame;

	private ImageListFetcher imageListFetcher;

	@Inject
	public Application(EventBus eventBus, MainFrame mainFrame,
			ImageUploader imageUploader, ImageListFetcher imageListFetcher) {
		this.mainFrame = mainFrame;
		this.imageListFetcher = imageListFetcher;

		eventBus.register(imageUploader);

		askForServerIp();
	}

	private void askForServerIp() {
		BASEURL = JOptionPane.showInputDialog("Input server api adress:",
				BASEURL);

		if (BASEURL == null || BASEURL.trim().equals("")) {
			JOptionPane.showMessageDialog(mainFrame.getFrame(),
					"Can't continue without a valid server api address.");

			System.exit(1);
		}
	}

	public void run() {
		imageListFetcher.importAllImages();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.display();
			}
		});
	}
}
