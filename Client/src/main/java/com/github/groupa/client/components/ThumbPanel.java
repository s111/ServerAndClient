package com.github.groupa.client.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
	private List<Thumb> thumbs = null;
	private MigLayout layout = new MigLayout("wrap 4");
	private EventBus eventBus;
	private GridView gridView;

	public ThumbPanel(EventBus eventBus, GridView gridView) {
		super();
		this.eventBus = eventBus;
		this.gridView = gridView;
		eventBus.register(this);
		setLayout(layout);
		addListeners();
	}
	
	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.hasSwitched() && View.GRID_VIEW.equals(event.getView())) {
			generateThumbs();
			addThumbsToPanel();
		}
	}
	
	@Subscribe
	public void libraryAddImageListener(LibraryAddEvent event) {
		if (event.getLibrary().equals(gridView.getLibrary())) {
			ImageObject image = event.getImage();
			Thumb thumb = new Thumb(image);
			thumbs.add(thumb);
			addThumbToPanel(thumb);
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

	private void addListeners() {
		addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
			}
		});
	}

	private void addThumbsToPanel() {
		removeAll();
		for (Thumb thumb : thumbs) {
			addThumbToPanel(thumb);
		}
	}

	private void addThumbToPanel(Thumb thumb) {
		Component component = thumb.getSmallThumb();
		add(component);
		component.addMouseListener(new ThumbListener(thumb));
	}
	
	private void generateThumbs() {
		Library library = gridView.getLibrary();
		if (library == null) return;
		int imageCount = library.imageCount();
		thumbs = new ArrayList<>();
		for (int i = 0; i < imageCount; i++) {
			ImageObject image = library.getImage(i);
			Thumb thumb = new Thumb(image);
			thumbs.add(thumb);
		}
	}

	private class Thumb {
		protected ImageObject img;
		private JLabel label;
		private Image small = null;
		private Image large = null;

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
						small = image;
						label.setText("");
						label.setIcon(new ImageIcon(image.getScaledInstance(
								130, -1, Image.SCALE_FAST)));
					}

					@Override
					public void failure() {
						label.setText("Error loading image");
					}
				});
			}
			return label;
		}

		public JLabel getLargeThumb() {
			if (large == null) {
				img.loadImageWithCallback(new Callback<Image>() {
					@Override
					public void success(Image image) {
						large = image;
						label.setText("");
						label.setIcon(new ImageIcon(image.getScaledInstance(
								130, -1, Image.SCALE_FAST)));
					}

					@Override
					public void failure() {
						label.setText("Error loading image");
					}
				});
			}
			return label;
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
					
					JLabel label = thumb.getLargeThumb();
					Border border = BorderFactory.createLineBorder(Color.blue, 2);
					label.setBorder(border);
					if (!arg0.isControlDown()) {
						for (Thumb thumb : thumbs) {
							if (!thumb.equals(this.thumb)) {
								thumb.getSmallThumb().setBorder(null);
							}
						}
					} else {
						for (Thumb thumb : thumbs) {
							if (!thumb.equals(this.thumb)) {
								thumb.getSmallThumb();
							}
						}
					}
				}
				if (arg0.getClickCount() == 2) {
					eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW, thumb.img));
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
