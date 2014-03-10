package com.github.groupa.client.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;

public class GridView {
	private Library library;

	private JPanel mainPanel;
	private JPanel thumbPanel;
	private MigLayout layout;
	private int imageCount = -1;
	private List<Thumb> thumbs = new ArrayList<>();

	public GridView(Library library) {
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

	// App.mainFrame.setNewView(new ImageView(library, mainFrame).getPanel());

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
			Thumb thumb = new Thumb(library.getImage(i), i);
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
				small = toLabel(150, img.getImageRaw());
			}
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
