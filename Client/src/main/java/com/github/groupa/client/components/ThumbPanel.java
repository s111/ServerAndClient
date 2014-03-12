package com.github.groupa.client.components;

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

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.MainFrame;
import com.github.groupa.client.views.ImageView;
import com.github.groupa.client.views.View;
import com.google.inject.Inject;

public class ThumbPanel extends JPanel implements Scrollable {
	private final List<Thumb> thumbs = new ArrayList<>();
	private Library library;
	private ImageView imageView;
	private MainFrame mainFrame;
	private MigLayout layout = new MigLayout("wrap 4");

	@Inject
	public ThumbPanel(Library library, ImageView imageView, MainFrame mainFrame) {
		super();
		this.library = library;
		this.imageView = imageView;
		this.mainFrame = mainFrame;
		setLayout(layout);
		generateThumbs();
		addThumbsToPanel();
		addListeners();
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
			Component component = thumb.getSmallThumb();
			add(component);
			component.addMouseListener(new ThumbListener(thumb));
		}
	}

	private void generateThumbs() {
		int imageCount = library.imageCount();
		thumbs.clear();
		for (int i = 0; i < imageCount; i++) {
			ImageObject image = library.getImage(i);
			Thumb thumb = new Thumb(image, i);
			thumbs.add(thumb);
		}
	}

	private class Thumb {
		protected ImageObject img;
		protected int idx;
		private JLabel small = null;
		private JLabel large = null;

		public Thumb(ImageObject img, int idx) {
			this.img = img;
			this.idx = idx;
		}

		public JLabel getSmallThumb() {
			if (small == null) {
				small = new JLabel("image not loaded");
			}

			img.loadImageWithCallback(new Callback<Image>() {
				@Override
				public void success(Image image) {
					small.setText("");
					small.setIcon(new ImageIcon(image.getScaledInstance(130,
							-1, Image.SCALE_FAST)));
				}

				@Override
				public void failure() {
				}
			});

			return small;
		}

		public JLabel getLargeThumb() {
			if (large == null) {
				large = toLabel(300, img.getImageRaw());
			}
			return large;
		}

		private JLabel toLabel(int width, Image image) {
			JLabel label = new JLabel();
			label.setIcon(new ImageIcon(image.getScaledInstance(width, -1,
					Image.SCALE_FAST)));

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
				if (arg0.getClickCount() == 2) {
					library.setActiveImage(thumb.img);
					mainFrame.showView(View.IMAGE_VIEW);
					imageView.activateImageView();
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
}
