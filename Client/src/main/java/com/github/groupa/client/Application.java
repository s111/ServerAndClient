package com.github.groupa.client;

import java.net.ConnectException;

import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.components.MenuBar;
import com.github.groupa.client.jsonobjects.ImageList;
import com.github.groupa.client.jsonobjects.ImageShort;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.GridView;
import com.github.groupa.client.views.ImageView;
import com.github.groupa.client.views.View;

public class Application {
	private static final Logger logger = LoggerFactory
			.getLogger(Application.class);

	public static String serverAPIBaseURL = "http://localhost:9000/api";

	private MainFrame mainFrame;

	private Library library;

	private RESTService restService;

	@Inject
	public Application(Library library) {
		this.library = library;

		BasicConfigurator.configure();

		trySettingANativeLookAndFeel();
		askForBaseURL();

		restService = Main.injector.getInstance(RESTService.class);

		setUpLibrary();
		setUpGUI();
	}

	public void start() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.display();
			}
		});
	}

	private void trySettingANativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			// If system look and feel can't be found, swing should just use the
			// crossplatform one
		}
	}

	private void askForBaseURL() {
		serverAPIBaseURL = JOptionPane.showInputDialog("Server API BaseURL",
				serverAPIBaseURL);

		if (serverAPIBaseURL == null) {
			System.exit(0);
		}
	}

	private void setUpLibrary() {
		ImageList imageList = null;

		try {
			imageList = restService.getImageList(0, 0);
		} catch (ConnectException exception) {
			logger.error("Could not connect to the server: "
					+ exception.getMessage());
		}

		if (imageList == null) {
			return;
		}

		for (ImageShort image : imageList.getImages()) {
			ImageObject imageObject = Main.injector
					.getInstance(ImageObject.class);
			imageObject.setId(image.getId());

			library.add(imageObject);
		}
	}

	private void setUpGUI() {
		mainFrame = Main.injector.getInstance(MainFrame.class);
		mainFrame.setTitle("App");

		MenuBar menuBar = Main.injector.getInstance(MenuBar.class);

		mainFrame.setMenuBar(menuBar.getMenuBar());

		setUpViews();
	}

	private void setUpViews() {
		ImageView imageView = Main.injector.getInstance(ImageView.class);
		GridView gridView = Main.injector.getInstance(GridView.class);

		mainFrame.addView(imageView.getPanel(), View.IMAGE_VIEW);
		mainFrame.addView(gridView.getPanel(), View.GRID_VIEW);
		mainFrame.showView(View.GRID_VIEW);
	}
}
