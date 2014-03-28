package com.github.groupa.client.gui.panels;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.components.ImagePanel;
import com.github.groupa.client.events.DisplayedImageChangedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.gui.ActiveImage;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ImageContentPanel implements ContentPanel {
	private JPanel panel = new JPanel();

	private Library library;

	private EventBus eventBus;

	private ActiveImage activeImage;

	private ImagePanel imagePanel;

	private int currentImageIndex = -1;

	@Inject
	public ImageContentPanel(Library library, EventBus eventBus,
			ActiveImage activeImage, ImagePanel imagePanel) {
		this.library = library;
		this.eventBus = eventBus;
		this.activeImage = activeImage;
		this.imagePanel = imagePanel;

		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		panel.add(imagePanel.getPanel(), "grow, push, wrap");

		NavigationPanel navigationPanel = new NavigationPanel();

		navigationPanel.setNextAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setImage(currentImageIndex + 1);
			}
		});

		navigationPanel.setPreviousAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setImage(currentImageIndex - 1);
			}
		});

		navigationPanel.setUpAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ImageContentPanel.this.eventBus.post(new SwitchViewEvent(
						View.GRID_VIEW));
			}
		});

		panel.add(navigationPanel.getPanel(), "growx");
	}

	private void setImage(int index) {
		int count = library.imageCount();

		if (count == 0) {
			return;
		}

		currentImageIndex = (count + index) % count;

		activeImage.setCurrentImageIndex(currentImageIndex);

		ImageObject activeImageObject = library.getImage(currentImageIndex);

		eventBus.post(new DisplayedImageChangedEvent(activeImageObject));

		activeImageObject.loadImageWithCallback(new Callback<Image>() {
			@Override
			public void success(Image image) {
				imagePanel.setImage(image);
			}

			@Override
			public void failure() {
			}
		});
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Subscribe
	public void libraryAddImageListener(LibraryAddEvent event) {
		if (currentImageIndex == -1 && event.getLibrary().equals(library)) {
			setImage(0);
		}
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
