package com.github.groupa.client.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.components.Thumb;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.LibrarySortEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

@SuppressWarnings("serial")
public class ThumbPanel extends JPanel implements Scrollable {
	private GridLayout layout = new GridLayout(0, 2, 0, 0);

	private List<ImageObject> images = new ArrayList<>();
	private List<ImageObject> selectedImages = new ArrayList<>();

	private Map<ImageObject, Thumb> thumbs = new HashMap<>();

	private Border selectedThumbBorder = BorderFactory.createLineBorder(
			Color.blue, 2);
	private Border activeThumbBorder = BorderFactory.createLineBorder(
			Color.cyan, 2);

	private Border defaultThumbBorder = BorderFactory.createEmptyBorder(2, 2,
			2, 2);

	private String thumbSize = "m";

	private EventBus eventBus;
	private Library library;
	private ImageObject activeImage = null;

	private int prevWidth = 333; // Hacky but it works (atm)

	@Inject
	public ThumbPanel(EventBus eventBus, Library library) {
		super();
		this.eventBus = eventBus;
		this.library = library;
		eventBus.register(this);
		setLayout(layout);
		setLibrary(library);
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(640, 480);
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if (orientation == SwingConstants.VERTICAL) {
			return 120;
		}
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return 10;
	}

	public void widthChanged(int width) {
		if (images.isEmpty())
			return;
		this.prevWidth = width;
		setColumnCount(roomForColumns());
	}

	public List<ImageObject> getSelectedImages() {
		List<ImageObject> list = new ArrayList<>();
		list.addAll(selectedImages);
		return list;
	}

	public ImageObject getActiveImage() {
		return activeImage;
	}

	public void setActiveImage(ImageObject image) {
		if (image == activeImage || !images.contains(image))
			return;
		if (activeImage != null) {
			if (selectedImages.contains(activeImage)) {
				thumbs.get(activeImage).setBorder(selectedThumbBorder);
			} else {
				thumbs.get(activeImage).setBorder(defaultThumbBorder);
			}
		}
		activeImage = image;
		thumbs.get(activeImage).setBorder(activeThumbBorder);
	}

	public void setPanelThumbSize(String size) {
		if (this.thumbSize.equals(size)) return;
		this.thumbSize = size;
		reAddThumbsToPanel();
		setColumnCount(roomForColumns());
	}

	public void setLibrary(Library library) {
		this.library = library;
		this.images.clear();
		this.selectedImages.clear();
		activeImage = null;
		removeAll();
		addImages(library.getImages());
	}

	public Library getLibrary() {
		return library;
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
	public void libraryAddImageListener(LibraryAddEvent event) {
		if (event.getLibrary().equals(library)) {
			ImageObject image = event.getImage();
			if (image == null) {
				addImages(event.getImages());
			} else {
				addImage(image);
			}
			Comparator<ImageObject> cmp = library.getComparator();
			if (cmp != null) {
				Collections.sort(images, cmp);
				reAddThumbsToPanel();
			}
		}
	}

	@Subscribe
	public void LibrarySortListener(LibrarySortEvent event) {
		if (event.getLibrary().equals(library)) {
			Collections.sort(images, library.getComparator());
			reAddThumbsToPanel();
		}
	}

	private int roomForColumns() {
		int size = images.get(0).thumbSize.get(this.thumbSize) + 4;
		int columns = (prevWidth - 20) / size;
		return columns;
	}
	
	private void setColumnCount(int columns) {
		if (images.isEmpty())
			return;
		if (columns == layout.getColumns()) return;
		layout = new GridLayout(0, columns, 0, 0);
		setLayout(layout);
		revalidate();
	}

	private void reAddThumbsToPanel() {
		removeAll();
		for (ImageObject image : images) {
			Thumb thumb = thumbs.get(image);
			add(thumb.getThumb(thumbSize));
		}
		revalidate();
	}

	private void addImages(List<ImageObject> list) {
		for (ImageObject img : list) { // TODO: Make thread safe
			addImage(img);
		}
	}

	private void addImage(ImageObject image) {
		images.add(image);
		Thumb thumb = new Thumb(image) {
			@Override
			public void singleClick() {
				ThumbPanel.this.deselectImages();
				ThumbPanel.this.setActiveImage(getImageObject());
			}

			@Override
			public void ctrlClick() {
				if (ThumbPanel.this.activeImage != null
						&& ThumbPanel.this.selectedImages.isEmpty()) {
					ThumbPanel.this.selectImage(ThumbPanel.this.activeImage);
				}
				if (ThumbPanel.this.selectedImages.contains(getImageObject())) {
					ThumbPanel.this.deselectImage(getImageObject());
				} else {
					ThumbPanel.this.selectImage(getImageObject());
				}
				ThumbPanel.this.setActiveImage(getImageObject());
			}

			@Override
			public void doubleClick() {
				eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW,
						getImageObject(), ThumbPanel.this.getLibrary()));
			}
		};
		thumbs.put(image, thumb);
		add(thumb.getThumb(thumbSize));
		revalidate();
	}

	private void deselectImages() {
		List<ImageObject> list = new ArrayList<>();
		list.addAll(selectedImages);
		for (ImageObject image : list) {
			deselectImage(image);
		}
	}

	private void deselectImage(ImageObject image) {
		thumbs.get(image).setBorder(defaultThumbBorder);
		selectedImages.remove(image);
	}

	private void selectImage(ImageObject image) {
		thumbs.get(image).setBorder(selectedThumbBorder);
		selectedImages.add(image);
	}
}
