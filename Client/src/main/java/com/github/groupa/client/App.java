package com.github.groupa.client;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import servercommunication.HTTPRequester;
import servercommunication.Requester;

import com.github.groupa.client.jsonobjects.Image;
import com.github.groupa.client.jsonobjects.ImageList;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	private static void getImages(String host) throws IOException {
		Requester requester = new HTTPRequester(host);
		ImageList imageList = requester.getImageList(0);
		if (imageList == null) return;
		for (Image image : imageList.images) {
			Library.add(new ImageObject(image.id, requester));
		}
	
	}

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
				String host = JOptionPane.showInputDialog("Host address",
						"localhost:9000");

				mainFrame.display();

				try {
					getImages(host);
				} catch (IOException e) {
					logger.warn("Could not import image due to IOException");
				}

				mainFrame.replaceContent(new ImageView().getPanel());
			}
		});
	}
}
