package com.github.groupa.client;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		/*
		 * Quick and simple way to configure slf4j Should probably use a config
		 * as we expand
		 */
		BasicConfigurator.configure();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame mainFrame = new MainFrame("App");

				mainFrame.display();
				
				Requester requester = new ImageRequester();

				try {
					Library.add(1, requester.requestImage(1));
					Library.add(2, requester.requestImage(2));
				} catch (IOException e) {
					logger.warn("Could not import image due to IOException");
				}
				
				ImageObject imageObject = Library.get(1);
				
				if (imageObject != null) {
					mainFrame.setImageView(imageObject);
				}
			}
		});
	}
}
