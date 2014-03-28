package com.github.groupa.client.gui.panels;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.events.ActiveLibraryChangedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class GridPanel implements Panel {
	private JPanel panel;
	private ThumbPanel thumbPanel;
	private EventBus eventBus = null;
	private Library library = null;
	private List<Thumb> thumbs = new ArrayList<>();
	private List<Thumb> selectedThumbs = new ArrayList<>();
	private String panelThumbSize = "m";
	private String previewThumbSize = "xl";

	@Inject
	public GridPanel(EventBus eventBus, Library library) {
		this.eventBus = eventBus;
		this.library = library;
		eventBus.register(this);
		setUpImageViewer();
		setLibrary(library);
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	public void setPanelThumbSize(String size) {
		panelThumbSize = size;
	}

	public void setPreviewThumbSize(String size) {
		previewThumbSize = size;
	}

	public String getPreviewThumbSize() {
		return previewThumbSize;
	}

	public String getPanelThumbSize() {
		return panelThumbSize;
	}

	public void setLibrary(Library library) {
		this.library = library;
		libraryChanged();
	}

	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.hasSwitched() && View.GRID_VIEW.equals(event.getView())) {
			Library lib = event.getLibrary();
			if (lib != null && !lib.equals(library)) {
				setLibrary(lib);
			}
		}
	}

	@Subscribe
	public void activeLibrarychangeListener(ActiveLibraryChangedEvent event) {
		Library library = event.getLibrary();

		if (!library.equals(this.library)) {
			setLibrary(library);
		}
	}

	@Subscribe
	public void libraryAddImageListener(LibraryAddEvent event) {
		if (event.getLibrary().equals(library)) {
			ImageObject image = event.getImage();
			if (image == null) { // Many new images
				for (ImageObject img : event.getImages()) {
					addImage(img);
				}
			} else { // One new image
				addImage(image);
			}
		}
	}

	public class Thumb {
		private ImageObject imageObject;
		private Map<String, JLabel> labels;

		public Thumb(ImageObject img) {
			this.imageObject = img;
			labels = new HashMap<String, JLabel>();
		}

		public ImageObject getImageObject() {
			return imageObject;
		}

		public JLabel getThumb(String size) {
			JLabel label = labels.get(size);
			if (label == null) {
				final JLabel newLabel = new JLabel();
				newLabel.setText("Not loaded");
				label = newLabel;
				labels.put(size, label);
				imageObject.loadThumbWithCallback(new Callback<Image>() {
					@Override
					public void success(Image image) {
						newLabel.setText("");
						newLabel.setIcon(new ImageIcon(image));
					}

					@Override
					public void failure() {
						newLabel.setText("Error loading image");
					}
				}, size);
			}
			return label;
		}
	}

	private void setUpImageViewer() {
		MigLayout layout = new MigLayout("fill");
		panel = new JPanel(layout);
		thumbPanel = new ThumbPanel(eventBus, panelThumbSize);
		final JScrollPane thumbScroll = new JScrollPane(thumbPanel);
		thumbScroll.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				thumbPanel.widthChanged(thumbScroll.getWidth());
			}
		});
		panel.add(thumbScroll, "grow");

	}

	private void addImage(ImageObject image) {
		Thumb thumb = new Thumb(image);
		thumbs.add(thumb);
		thumbPanel.addThumb(thumb);
	}

	private void libraryChanged() {
		thumbs.clear();
		selectedThumbs.clear();
		thumbPanel.libraryChanged(library);
		for (ImageObject image : library.getImages()) {
			addImage(image);
		}
	}
}
