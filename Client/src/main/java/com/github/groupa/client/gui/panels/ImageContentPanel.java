package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.events.ImageModifiedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.servercommunication.ModifyImage;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ImageContentPanel implements ContentPanel {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageContentPanel.class);

	private JPanel panel = new JPanel();

	private EventBus eventBus;

	private ImagePanel imagePanel;

	private int currentImageIndex = -1;

	private List<ImageObject> images = new ArrayList<>();

	private ImageSidebarPanel imageSidebarPanel;

	private Library library;

	private ModifyImage modifyImage;

	@Inject
	public ImageContentPanel(ModifyImage modifyImage, EventBus eventBus,
			ImagePanel imagePanel, ImageSidebarPanel imageSidebarPanel) {
		this.modifyImage = modifyImage;
		this.eventBus = eventBus;
		this.imagePanel = imagePanel;
		this.imageSidebarPanel = imageSidebarPanel;

		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		panel.add(imagePanel, "grow, push, wrap");

		NavigationPanel navigationPanel = new NavigationPanel();

		navigationPanel.setNextAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setImage(currentImageIndex + 1);
			}
		});

		navigationPanel.setPreviousAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setImage(currentImageIndex - 1);
			}
		});

		navigationPanel.setUpAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ImageContentPanel.this.eventBus.post(new SwitchViewEvent(
						View.GRID_VIEW));
			}
		});

		navigationPanel.setCWAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				rotateImage(images.get(currentImageIndex), 90);
			}
		});

		navigationPanel.setCCWAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				rotateImage(images.get(currentImageIndex), -90);
			}
		});

		panel.add(navigationPanel.getPanel(), "growx");

		addKeyBindings();
	}

	private void rotateImage(ImageObject image, int angle) {
		modifyImage.rotate(new Callback<ImageObject>() {

			@Override
			public void success(ImageObject t) {

			}

			@Override
			public void failure() {
				logger.warn("Error rotating image");
			}

		}, image, angle);

	}

	@SuppressWarnings("serial")
	private void addKeyBindings() {
		InputMap inputMap = panel
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = panel.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "keyLeft");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "keyRight");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				"previousView");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				"previousView");

		actionMap.put("keyLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setImage(currentImageIndex - 1);
			}
		});

		actionMap.put("keyRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setImage(currentImageIndex + 1);
			}
		});
		actionMap.put("previousView", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventBus.post(new SwitchViewEvent(View.GRID_VIEW));
			}
		});
	}

	private void setImage(int index) {
		int count = images.size();

		if (count == 0) {
			return;
		}

		currentImageIndex = (count + index) % count;

		final ImageObject activeImageObject = images.get(currentImageIndex);
		if (activeImageObject == null)
			return;
		activeImageObject.loadImage(new Callback<BufferedImage>() {
			@Override
			public void success(BufferedImage image) {
				imagePanel.setImage(image);
				imageSidebarPanel.setImage(activeImageObject);
			}

			@Override
			public void failure() {
			}
		}, "compressed");
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.hasSwitched() && View.IMAGE_VIEW.equals(event.getView())) {
			ImageObject img = event.getImageObject();
			Library lib = event.getLibrary();
			if (lib != null)
				setLibrary(lib);
			if (img != null) {
				currentImageIndex = images.indexOf(img);
			}
			setImage(currentImageIndex);
		}
	}

	@Subscribe
	public void libraryAddImageListener(LibraryAddEvent event) {
		if (event.getLibrary().equals(library)) {
			ImageObject image = event.getImage();
			if (image == null) {
				images.addAll(event.getImages());
			} else {
				images.add(image);
			}
		}
	}

	@Subscribe
	public void imageModifiedListener(ImageModifiedEvent event) {
		if (images.indexOf(event.getImageObject()) == currentImageIndex) {
			setImage(currentImageIndex);
		}
	}

	private void setLibrary(Library lib) {
		this.library = lib;
		ImageObject prevImg = null;
		if (currentImageIndex >= 0 && images.contains(currentImageIndex))
			prevImg = images.get(currentImageIndex);
		images.clear();
		images.addAll(lib.getImages());
		setImage(images.indexOf(prevImg));
	}
}
