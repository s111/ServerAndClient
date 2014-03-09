package com.github.groupa.client;

import java.net.ConnectException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RestAdapter;

import com.github.groupa.client.jsonobjects.ImageList;
import com.github.groupa.client.jsonobjects.ImageShort;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.GridView;
import com.github.groupa.client.views.ImageView;
import com.github.groupa.client.views.View;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	private static String serverAPIBaseURL = "http://localhost:9000/api";

	private static RESTService restService;

	private static MainFrame mainFrame;

	public static void main(String[] args) {
		BasicConfigurator.configure();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
		}

		serverAPIBaseURL = JOptionPane.showInputDialog("Server API BaseURL",
				serverAPIBaseURL);

		if (serverAPIBaseURL == null)
			return;

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(serverAPIBaseURL)
				.setErrorHandler(new RESTErrorHandler()).build();

		restService = restAdapter.create(RESTService.class);

		getImages(serverAPIBaseURL);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame = new MainFrame("App");

				mainFrame.display();
				mainFrame.addView(
						new ImageView(new Library(), mainFrame).getPanel(),
						View.IMAGE_VIEW);
				mainFrame.addView(
						new GridView(new Library(), mainFrame).getPanel(),
						View.GRID_VIEW);

				mainFrame.showView(View.GRID_VIEW);
			}
		});
	}

	private static void getImages(String serverAPIBaseURL) {
		ImageList imageList = null;

		try {
			imageList = restService.getImageList();
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(mainFrame.getFrame(),
					"Failed to load initial set of images");

			logger.error("Could not connect to the server: " + e.getMessage());
		}

		if (imageList == null) {
			return;
		}

		for (ImageShort image : imageList.getImages()) {
			Library.add(new ImageObject(image.getId(), restService));
		}

	}
}
