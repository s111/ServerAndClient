package com.github.groupa.client.factories;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;

public abstract class ThumbMenuFactory {
	public static JPopupMenu getMenu(EventBus eventBus, ImageObject image,
			List<ImageObject> selectedImages) {
		JPopupMenu menu = new JPopupMenu();

		menu.add(showImageItem(eventBus, image, selectedImages));
		return menu;
	}

	private static JMenuItem showImageItem(final EventBus eventBus,
			final ImageObject image, final List<ImageObject> selectedImages) {
		JMenuItem item = new JMenuItem("Show in imageview");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showClickedImage(eventBus, image, selectedImages);
			}
		});
		return item;
	}

	private static void showClickedImage(EventBus eventBus, ImageObject image,
			List<ImageObject> selectedImages) {
		eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW, image));
	}
}
