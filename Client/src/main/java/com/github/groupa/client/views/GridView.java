package com.github.groupa.client.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.MainFrame;

public class GridView {
	private Library library;

	private JPanel mainPanel;
	private JPanel thumbPanel;
	private MigLayout layout;
	private int imageCount = -1;
	private List<Thumb> thumbs = new ArrayList<>();

	private MainFrame mainFrame;

	@Inject
	public GridView(MainFrame mainFrame, Library library) {
		this.mainFrame = mainFrame;
		this.library = library;

		setUpImageViewer();
		generateThumbs();
		addThumbsToPanel();
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	private void setUpImageViewer() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		thumbPanel = new JPanel();
		layout = new MigLayout("wrap 4");
		thumbPanel.setLayout(layout);
		mainPanel.add(thumbPanel, BorderLayout.SOUTH);
	}

	private void addThumbsToPanel() {
		thumbPanel.removeAll();
		for (Thumb thumb : thumbs) {
			Component component = thumb.getSmallThumb();
			thumbPanel.add(component);
		}
	}

	private void generateThumbs() {
		imageCount = library.imageCount();
		thumbs.clear();
		for (int i = 0; i < imageCount; i++) {
			ImageObject image = library.getImage(i);
			Thumb thumb = new Thumb(image, image.getId());
			thumbs.add(thumb);
		}
	}

	private class Thumb {
		protected ImageObject img;
		protected long idx;
		private JLabel small = null;
		private JLabel large = null;

		public Thumb(ImageObject img, long idx) {
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
					small.setIcon(new ImageIcon(image.getScaledInstance(150,
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
}
