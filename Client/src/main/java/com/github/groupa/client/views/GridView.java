package com.github.groupa.client.views;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.components.SearchField;
import com.github.groupa.client.components.ThumbPanel;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class GridView {
	private JPanel mainPanel;
	private ThumbPanel thumbPanel;
	private EventBus eventBus = null;
	private Library library = null;
	private List<Thumb> thumbs = new ArrayList<>();
	private Thumb selectedThumb = null;
	private String thumbSize = "l";

	@Inject
	public GridView(EventBus eventBus, SingleLibrary library) {
		this.eventBus = eventBus;
		this.library = library;
		eventBus.register(this);
		setUpImageViewer();
		setLibrary(library);
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public void setThumbSize(String size) {
		thumbSize = size;
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
		protected ImageObject img;
		private JLabel label;
		private ImageIcon small = null;
		private ImageIcon large = null;

		public Thumb(ImageObject img) {
			this.img = img;
			label = new JLabel();
			label.setText("Image not loaded");
			label.addMouseListener(new ThumbListener(this));
		}

		public JLabel getLabel() {
			return label;
		}

		public JLabel getSmallThumb() {
			if (small == null) {
				img.loadThumbWithCallback(new Callback<Image>() {
					@Override
					public void success(Image image) {
						small = new ImageIcon(image);
						label.setText("");
						label.setIcon(small);
					}

					@Override
					public void failure() {
						label.setText("Error loading image");
					}
				}, thumbSize, true);
			} else
				label.setIcon(small);
			return label;
		}

		public JLabel getLargeThumb() {
			if (large == null) {
				img.loadThumbWithCallback(new Callback<Image>() {
					@Override
					public void success(Image image) {
						large = new ImageIcon(image);
						label.setText("");
						label.setIcon(large);
					}

					@Override
					public void failure() {
						label.setText("Error loading image");
					}
				}, thumbSize, false);
			} else
				label.setIcon(large);
			return label;
		}
	}

	private void setUpImageViewer() {
		mainPanel = new JPanel(new BorderLayout());
		thumbPanel = new ThumbPanel();
		mainPanel.add(new JScrollPane(thumbPanel), BorderLayout.CENTER);
		mainPanel.add(new SearchField(eventBus, library, this).getPanel(),
				BorderLayout.NORTH);
		mainPanel.add(new GridBottomPanel(this).getPanel(), BorderLayout.SOUTH);
	}

	private void addImage(ImageObject image) {
		Thumb thumb = new Thumb(image);
		thumbs.add(thumb);
		thumbPanel.addThumb(thumb);
	}

	private void libraryChanged() {
		thumbPanel.libraryChanged();
		if (library.imageCount() == 0) {
			thumbPanel.repaint();
		} else {
			thumbs = new ArrayList<>();
			for (ImageObject image : library.getImages()) {
				addImage(image);
			}
		}
	}

	private void deselectThumb(Thumb thumb) {
		selectedThumb = null;
		thumbPanel.deselectThumb(thumb);
	}

	private void selectThumb(Thumb thumb) {
		selectedThumb = thumb;
		thumbPanel.selectThumb(thumb);
	}

	private void imageClicked(Thumb thumb) {
		if (selectedThumb == thumb) {
			deselectThumb(thumb);
		} else {
			if (selectedThumb != null) {
				deselectThumb(selectedThumb);
			}
			selectThumb(thumb);
		}
	}

	private void imageDoubleClicked(Thumb thumb) {
		eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW, thumb.img, library));
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
					// arg0.isControlDown() will be used later for multiple
					// selections
					imageClicked(thumb);
				} else if (arg0.getClickCount() == 2) {
					imageDoubleClicked(thumb);
				}
			}
		}
	}
}
