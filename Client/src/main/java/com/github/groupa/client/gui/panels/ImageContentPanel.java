package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.ConnectException;

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

import com.github.groupa.client.ActiveImage;
import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.events.ActiveImageChangedEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ImageContentPanel implements ContentPanel {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageContentPanel.class);

	private JPanel panel = new JPanel();

	private Library library;

	private RESTService restService;

	private EventBus eventBus;

	private ActiveImage activeImage;

	private ImagePanel imagePanel;

	private int currentImageIndex = -1;

	@Inject
	public ImageContentPanel(Library library, RESTService restService,
			EventBus eventBus, ActiveImage activeImage, ImagePanel imagePanel) {
		this.library = library;
		this.restService = restService;
		this.eventBus = eventBus;
		this.activeImage = activeImage;
		this.imagePanel = imagePanel;

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
				ImageContentPanel.this.imagePanel.rotateCW();

				try {
					ImageContentPanel.this.restService.rotateImage(
							ImageContentPanel.this.activeImage.getImage()
									.getId(), 90);
				} catch (ConnectException exception) {
					logger.warn("Could not connect to the server and rotate image: "
							+ exception.getMessage());
				}
			}
		});

		navigationPanel.setCCWAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ImageContentPanel.this.imagePanel.rotateCCW();

				try {
					ImageContentPanel.this.restService.rotateImage(
							ImageContentPanel.this.activeImage.getImage()
									.getId(), -90);
				} catch (ConnectException exception) {
					logger.warn("Could not connect to the server and rotate image: "
							+ exception.getMessage());
				}
			}
		});

		panel.add(navigationPanel.getPanel(), "growx");

		addKeyBindings();
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
		int count = library.imageCount();

		if (count == 0) {
			return;
		}

		currentImageIndex = (count + index) % count;

		activeImage.setCurrentImageIndex(currentImageIndex);

		final ImageObject activeImageObject = library
				.getImage(currentImageIndex);

		activeImageObject.loadImage(new Callback<BufferedImage>() {
			@Override
			public void success(BufferedImage image) {
				imagePanel.setImage(image);

				eventBus.post(new ActiveImageChangedEvent(activeImageObject));
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
			if (img != null) {
				Library lib = event.getLibrary();
				if (lib != null)
					library = lib;
				if (library == null)
					return;
				int idx = library.indexOf(img);
				if (idx < 0)
					return;
				currentImageIndex = idx;
			}
			setImage(currentImageIndex);
		}
	}
}
