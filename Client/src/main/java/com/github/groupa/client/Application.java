package com.github.groupa.client;

import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.components.MenuBar;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.views.GridView;
import com.github.groupa.client.views.ImageView;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;

public class Application {
	private static final Logger logger = LoggerFactory
			.getLogger(Application.class);

	public static String serverAPIBaseURL = "http://localhost:9000/api";

	private MainFrame mainFrame;

	private Library library;

	private EventBus eventBus;

	@Inject
	public Application(EventBus eventBus, SingleLibrary library) {
		this.eventBus = eventBus;
		this.library = library;
	}

	public void start() {
		setUp();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.display();
			}
		});
	}

	private void setUp() {
		eventBus.register(Main.injector.getInstance(ImageUploader.class));

		trySettingANativeLookAndFeel();
		askForBaseURL();

		setUpGUI();
	}

	private void trySettingANativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			// If system look and feel can't be found, swing should just use the
			// crossplatform one
			logger.warn("Could not set system look and feel");
		}
	}

	private void askForBaseURL() {
		serverAPIBaseURL = JOptionPane.showInputDialog("Server API BaseURL",
				serverAPIBaseURL);

		if (serverAPIBaseURL == null) {
			logger.error("User did not enter base url. Can not continue");

			System.exit(0);
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
		imageView.setLibrary(library);
		GridView gridView = Main.injector.getInstance(GridView.class);
		gridView.setLibrary(library);
		mainFrame.addView(imageView.getPanel(), View.IMAGE_VIEW);
		mainFrame.addView(gridView.getPanel(), View.GRID_VIEW);
		eventBus.post(new SwitchViewEvent(View.GRID_VIEW));
	}
}
