package com.github.groupa.client.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.views.GridView;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public class ThumbPanel extends JPanel implements Scrollable {
	private List<Thumb> thumbs = new ArrayList<>();
	private MigLayout layout = new MigLayout("wrap 4");
	private EventBus eventBus;
	private GridView gridView;

	private Border selectedImageBorder = BorderFactory.createLineBorder(
			Color.blue, 2);

	private Border deselectedImageBorder = BorderFactory.createLineBorder(
			Color.black, 2);
	private Border defaultImageBorder = BorderFactory.createEmptyBorder(2, 2,
			2, 2);

	public ThumbPanel(EventBus eventBus, GridView gridView) {
		super();
		this.eventBus = eventBus;
		this.gridView = gridView;
		eventBus.register(this);
		setLayout(layout);
	}
	
	public void libraryChanged() {
		removeAll();
		Library library = gridView.getLibrary();
		if (library.imageCount() == 0) 
			repaint();
		else {
			thumbs = new ArrayList<>();
			for (ImageObject image : library.getImages()) {
				addImage(image);
			}
		}
	}

	private void addImage(ImageObject image) {
		addThumb(new Thumb(image));
	}

	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.hasSwitched() && View.GRID_VIEW.equals(event.getView())) {
			Library lib = event.getLibrary();
			if (gridView.getLibrary().equals(lib)) {
				gridView.setLibrary(lib);
			}
		}
	}

	@Subscribe
	public void libraryAddImageListener(LibraryAddEvent event) {
		if (event.getLibrary().equals(gridView.getLibrary())) {
			ImageObject image = event.getImage();
			if (image != null) {
				addImage(image);
			} else {
				for (ImageObject img : event.getImages()) {
					addImage(img);
				}
			}
		}
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

	private void addThumb(Thumb thumb) {
		thumb.getLabel().setBorder(defaultImageBorder);
		thumbs.add(thumb);
		Component component = thumb.getSmallThumb();
		add(component);
		component.addMouseListener(new ThumbListener(thumb));
	}

	private class Thumb {
		protected ImageObject img;
		private JLabel label;
		private ImageIcon small = null;
		private ImageIcon large = null;
		public boolean selected = false;

		public Thumb(ImageObject img) {
			this.img = img;
			label = new JLabel();
			label.setText("Image not loaded");
		}

		public JLabel getLabel() {
			return label;
		}

		public JLabel getSmallThumb() {
			if (small == null) {
				img.loadImageWithCallback(new Callback<Image>() {
					@Override
					public void success(Image image) {
						small = new ImageIcon(image.getScaledInstance(130, -1,
								Image.SCALE_FAST));
						label.setText("");
						label.setIcon(small);
					}

					@Override
					public void failure() {
						label.setText("Error loading image");
					}
				});
			} else
				label.setIcon(small);
			return label;
		}

		public JLabel getLargeThumb() {
			if (large == null) {
				img.loadImageWithCallback(new Callback<Image>() {
					@Override
					public void success(Image image) {
						large = new ImageIcon(image.getScaledInstance(260, -1,
								Image.SCALE_FAST));
						label.setText("");
						label.setIcon(large);
					}

					@Override
					public void failure() {
						label.setText("Error loading image");
					}
				});
			} else
				label.setIcon(large);
			return label;
		}
	}

	private void deselectThumb(Thumb thumb) {
		thumb.selected = false;
		thumb.getSmallThumb().setBorder(deselectedImageBorder);
	}

	private void selectedImage(Thumb thumb) {
		if (thumb.selected) {
			deselectThumb(thumb);
		} else {
			for (Thumb t : thumbs) { // Deselect other thumbs
				if (t.selected)
					deselectThumb(t);
			}
			thumb.getLargeThumb().setBorder(selectedImageBorder);
			thumb.selected = true;
		}
	}

	private class ThumbListener implements MouseListener {
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
					selectedImage(thumb);
				} else if (arg0.getClickCount() == 2) {
					eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW,
							thumb.img, gridView.getLibrary()));
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
}
