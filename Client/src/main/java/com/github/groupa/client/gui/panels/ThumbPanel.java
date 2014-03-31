package com.github.groupa.client.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.events.ActiveLibraryChangedEvent;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.events.LibrarySortEvent.LibrarySortEvent;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

@SuppressWarnings("serial")
public class ThumbPanel extends JPanel implements Scrollable {
	private GridLayout layout = new GridLayout(0, 2, 0, 0);

	private List<Thumb> thumbs = new ArrayList<>();
	private List<Thumb> selectedThumbs = new ArrayList<>();
	private Map<Thumb, ThumbListener> thumbListeners = new HashMap<>();

	private Border selectedThumbBorder = BorderFactory.createLineBorder(
			Color.cyan, 2);
	private Border currentThumbBorder = BorderFactory.createLineBorder(
			Color.blue, 2);

	private Border defaultThumbBorder = BorderFactory.createEmptyBorder(2, 2,
			2, 2);

	private String size = "m";

	private EventBus eventBus;
	private Library library;
	private Thumb currentThumb = null;

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
		if (thumbs.isEmpty())
			return;
		int currentColumns = layout.getColumns();
		int thumbSize = thumbs.get(0).getThumb(size).getWidth() + 3;
		int wantedColumns = width / thumbSize;
		int spare = width - wantedColumns * thumbSize;
		if (currentColumns < wantedColumns && spare > 5
				|| currentColumns > wantedColumns
				|| currentColumns == wantedColumns && spare < 5) {
			layout = new GridLayout(0, wantedColumns, 0, 0);
			setLayout(layout);

			revalidate();
		}
		repaint();
	}
	
	public List<ImageObject> getSelectedThumbs() {
		List<ImageObject> list = new ArrayList<>();
		for (Thumb thumb : selectedThumbs) {
			list.add(thumb.getImageObject());
		}
		return list;
	}

	public void setPanelThumbSize(String size) {
		this.size = size;
		setLibrary(library); //TODO: Reuse thumbs
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
			if (image == null) {
				addImages(event.getImages());
			} else {
				addImage(image);
			}
		}
	}
	
	@Subscribe
	public void LibrarySortListener(LibrarySortEvent event) {
		if (event.getLibrary().equals(library)) {
			setLibrary(library); //TODO: Reuse old thumbs
		}
	}
	
	private void setLibrary(Library library) {
		this.library = library;
		this.thumbs.clear();
		this.selectedThumbs.clear();
		removeAll();
		for (ImageObject img : library.getImages()) {
			addImage(img);
		}
		repaint();
	}

	private void addImage(ImageObject image) {
		Thumb thumb = new Thumb(image);
		thumbs.add(thumb);
		JLabel label = thumb.getThumb(size);
		setBorder(thumb, defaultThumbBorder);
		ThumbListener listener = new ThumbListener(thumb);
		label.addMouseListener(listener);
		thumbListeners.put(thumb, listener);
		add(label);
		repaint();
	}
	
	private void addImages(List<ImageObject> list) {
		for (ImageObject img : list) { //TODO: Make thread safe
			addImage(img);
		}
	}

	private void setCurrentThumb(Thumb thumb) {
		if (currentThumb != null) {
			if (selectedThumbs.contains(currentThumb)) {
				setBorder(currentThumb, selectedThumbBorder);
			} else {
				setBorder(currentThumb, defaultThumbBorder);
			}
		}
		currentThumb = thumb;
		setBorder(currentThumb, currentThumbBorder);
	}

	private void setBorder(Thumb thumb, Border border) {
		thumb.getThumb(size).setBorder(border);
	}

	private void deselectThumbs() {
		Iterator<Thumb> itr = selectedThumbs.iterator();
		while (itr.hasNext()) {
			Thumb thumb = itr.next();
			setBorder(thumb, defaultThumbBorder);
			itr.remove();
		}
	}

	private void imageDoubleClicked(Thumb thumb) {
		eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW, thumb
				.getImageObject(), library));
	}

	private void deselectThumb(Thumb thumb) {
		if (!selectedThumbs.contains(thumb))
			return;
		setBorder(thumb, defaultThumbBorder);
		selectedThumbs.remove(thumb);
	}

	private void selectThumb(Thumb thumb) {
		if (selectedThumbs.contains(thumb))
			return;
		setBorder(thumb, selectedThumbBorder);
		selectedThumbs.add(thumb);
	}

	private class ThumbListener extends MouseAdapter {
		private Thumb thumb;

		public ThumbListener(Thumb thumb) {
			this.thumb = thumb;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (arg0.getButton() == MouseEvent.BUTTON1) {
				if (arg0.getClickCount() == 1) {
					if (!arg0.isControlDown()) {
						deselectThumbs();
						setCurrentThumb(thumb);
					} else {
						if (selectedThumbs.contains(thumb)) {
							deselectThumb(thumb);
						} else {
							selectThumb(thumb);
						}
						setCurrentThumb(thumb);
					}
				} else if (arg0.getClickCount() == 2) {
					imageDoubleClicked(thumb);
				}
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
}
