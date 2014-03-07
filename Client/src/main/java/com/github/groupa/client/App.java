package com.github.groupa.client;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import retrofit.RestAdapter;

import com.github.groupa.client.jsonobjects.ImageList;
import com.github.groupa.client.jsonobjects.ImageShort;
import com.github.groupa.client.servercommunication.RESTService;

public class App {
	private static String serverAPIBaseURL = "http://localhost:9000/api";

	private static RESTService restService;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
		}

		serverAPIBaseURL = JOptionPane.showInputDialog("Server API BaseURL",
				serverAPIBaseURL);

		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				serverAPIBaseURL).build();

		restService = restAdapter.create(RESTService.class);

		getImages(serverAPIBaseURL);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame mainFrame = new MainFrame("App");

				mainFrame.display();
				mainFrame.replaceContent(new ImageView().getPanel());
			}
		});
	}

	private static void getImages(String serverAPIBaseURL) {
		ImageList imageList = restService.getImageList();

		if (imageList == null)
			return;

		for (ImageShort image : imageList.getImages()) {
			Library.add(new ImageObject(image.getId(), restService));
		}

	}
}
