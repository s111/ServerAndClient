package com.github.groupa.client.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.github.groupa.client.Library;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.views.View;
import com.github.groupa.client.views.GridView.Thumb;
import com.google.common.eventbus.EventBus;

@SuppressWarnings("serial")
public class ThumbPanel extends JPanel implements Scrollable {
	private GridLayout layout = new GridLayout(0, 4, 0, 0);

	private List<Thumb> thumbs = new ArrayList<>();
	private List<Thumb> selectedThumbs = new ArrayList<>();
	private Map<Thumb, ThumbListener> thumbListeners = new HashMap<>();

	private Border selectedThumbBorder = BorderFactory.createLineBorder(
			Color.cyan, 2);
	private Border currentThumbBorder = BorderFactory.createLineBorder(
			Color.blue, 2);

	private Border defaultThumbBorder = BorderFactory.createEmptyBorder(2, 2,
			2, 2);

	private String size;

	private EventBus eventBus;
	private Thumb currentThumb;

	private Library library;

	public ThumbPanel(EventBus eventBus, String size) {
		super();
		this.eventBus = eventBus;
		this.size = size;
		setLayout(layout);
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

	public void libraryChanged(Library library) {
		this.library = library;
		this.thumbs.clear();
		this.selectedThumbs.clear();
		removeAll();
		repaint();
	}

	public void addThumb(Thumb thumb) {
		thumbs.add(thumb);
		JLabel label = thumb.getThumb(size);
		setBorder(thumb, defaultThumbBorder);
		add(label);
		ThumbListener listener = new ThumbListener(thumb);
		label.addMouseListener(listener);
		thumbListeners.put(thumb, listener);
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

	public void widthChanged(int width) {
		if (thumbs.isEmpty())
			return;
		int currentColumns = layout.getColumns();
		int thumbSize = thumbs.get(0).getThumb(size).getWidth() + 3;
		int wantedColumns = width / thumbSize;
		int spare = width - wantedColumns*thumbSize;
		if (currentColumns < wantedColumns && spare > 5 
				|| currentColumns > wantedColumns 
				|| currentColumns == wantedColumns && spare < 5) {
			layout = new GridLayout(0, wantedColumns, 0, 0);
			setLayout(layout);
			//System.out.println(currentColumns + " : " + wantedColumns);
		} else {
			//System.out.println(currentColumns + " : " + wantedColumns + " : " + thumbSize*wantedColumns + "/" + width + " : " + spare);
		}
	}
}
