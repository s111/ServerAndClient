package com.github.groupa.client.main;

import javax.inject.Inject;
import javax.swing.SwingUtilities;

import com.github.groupa.client.ImageUploader;
import com.github.groupa.client.gui.frames.MainFrame;
import com.google.common.eventbus.EventBus;

public class Application {
	public static final String BASEURL = "http://localhost:9000/api";

	private MainFrame mainFrame;

	@Inject
	public Application(EventBus eventBus, MainFrame mainFrame,
			ImageUploader imageUploader) {
		this.mainFrame = mainFrame;

		eventBus.register(imageUploader);
	}

	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.display();
			}
		});
	}
}
