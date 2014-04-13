package com.github.groupa.client.factories;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;

public abstract class ThumbMenuFactory {
	public static JPopupMenu getMenu(EventBus eventBus, ImageObject image, Library library,
			List<ImageObject> selectedImages) {
		JPopupMenu menu = new JPopupMenu();

		menu.add(showImageItem(eventBus, image, library, selectedImages));
		return menu;
	}

	private static JMenuItem showImageItem(final EventBus eventBus,
			final ImageObject image, final Library library, final List<ImageObject> selectedImages) {
		JMenuItem item = new JMenuItem("Show in imageview");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showClickedImage(eventBus, image, library, selectedImages);
			}
		});
		return item;
	}

	private static void showClickedImage(EventBus eventBus, ImageObject image, Library library,
			List<ImageObject> selectedImages) {
		Library lib = library;
		if (!selectedImages.isEmpty() && selectedImages.contains(image)) {
			lib = new Library(eventBus, selectedImages);
		}
		eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW, image, lib));
	}
}
