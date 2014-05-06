package com.github.groupa.client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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

import com.github.groupa.client.BackgroundJob;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.ImageAvailableEvent;
import com.github.groupa.client.events.ImageModifiedEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.servercommunication.ModifyImage;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ImageContentPanel implements ContentPanel {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(ImageContentPanel.class);
	private JPanel panel = new JPanel();
	private EventBus eventBus;
	private ImagePanel imagePanel;
	private List<ImageObject> images = new ArrayList<>();
	private ImageSidebarPanel imageSidebarPanel;
	private ModifyImage modifyImage;
	private Library library;
	private ImageObject activeImageObject = null;

	@Inject
	public ImageContentPanel(ModifyImage modifyImage, EventBus eventBus,
			ImagePanel imagePanel, ImageSidebarPanel imageSidebarPanel,
			Library library) {
		this.modifyImage = modifyImage;
		this.eventBus = eventBus;
		this.imagePanel = imagePanel;
		this.imageSidebarPanel = imageSidebarPanel;
		this.library = library;

		MigLayout layout = new MigLayout();

		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		panel.add(imagePanel, "grow, push, wrap");

		NavigationPanel navigationPanel = new NavigationPanel();

		navigationPanel.setNextAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextImage();
			}
		});

		navigationPanel.setPreviousAction(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				previousImage();
			}
		});

		navigationPanel.setUpAction(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ImageContentPanel.this.eventBus.post(new SwitchViewEvent(
						View.GRID_VIEW));
			}
		});

		navigationPanel.setCWAction(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				rotateImage(activeImageObject, 90);
			}
		});

		navigationPanel.setCCWAction(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				rotateImage(activeImageObject, -90);
			}
		});

		panel.add(navigationPanel.getPanel(), "growx");
		addKeyBindings();
	}

	private void rotateImage(ImageObject image, int angle) {
		modifyImage.rotate(image, angle);
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
			public void actionPerformed(ActionEvent e) {
				previousImage();
			}
		});

		actionMap.put("keyRight", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				nextImage();
			}
		});
		actionMap.put("previousView", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				eventBus.post(new SwitchViewEvent(View.GRID_VIEW));
			}
		});
	}

	private void nextImage() {
		ImageObject newImage = null;
		if (images == null) {
			newImage = library.getNextImage(activeImageObject);
		} else {
			int idx = images.indexOf(activeImageObject);
			int size = images.size();
			if (idx != -1 && size > 1) {
				newImage = images.get((idx + size + 1) % size);
			}
		}
		setImage(newImage);
		if (!eagerLoad(1))
			eagerLoad(2);
	}

	private void previousImage() {
		ImageObject newImage = null;
		if (images == null) {
			newImage = library.getPrevImage(activeImageObject);
		} else {
			int idx = images.indexOf(activeImageObject);
			int size = images.size();
			if (idx != -1 && size > 1) {
				newImage = images.get((idx + size - 1) % size);
			}
		}
		setImage(newImage);
		if (!eagerLoad(-1))
			eagerLoad(-2);

	}

	private boolean eagerLoad(int offset) {
		ImageObject newImage = null;
		if (images == null) {
			newImage = library.getNextImage(activeImageObject);
		} else {
			int idx = images.indexOf(activeImageObject);
			int size = images.size();
			if (idx != -1 && size > 1) {
				newImage = images.get((idx + size + offset) % size);
			}
		}
		if (newImage == null || newImage.hasImage())
			return false;

		newImage.loadImage("compressed", BackgroundJob.LOW_PRIORITY);
		return true;
	}

	private void setImage(ImageObject img) {
		if (img == null)
			return;
		activeImageObject = img;
		if (img.hasImage()) {
			imagePanel.setImage(activeImageObject);
			imageSidebarPanel.setImage(activeImageObject);
		} else {
			img.loadImage("compressed", BackgroundJob.HIGH_PRIORITY);
		}
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.hasSwitched() && View.IMAGE_VIEW.equals(event.getView())) {
			ImageObject img = event.getImageObject();
			images = event.getImageList();
			if (images.size() <= 1) {
				images = null;
			}
			setImage(img);
			eagerLoad(1);
			eagerLoad(-1);
		}
	}

	@Subscribe
	public void imageModifiedListener(ImageModifiedEvent event) {
		if (event.getImageObject() == activeImageObject) {
			setImage(activeImageObject);
		}
	}

	@Subscribe
	public void imageAvailableListener(ImageAvailableEvent event) {
		if (event.getImageObject() == activeImageObject) {
			setImage(activeImageObject);
		}
	}
}
