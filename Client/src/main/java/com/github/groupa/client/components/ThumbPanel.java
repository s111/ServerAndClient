package com.github.groupa.client.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.miginfocom.layout.LC;
import net.miginfocom.layout.AC;
import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.views.GridView.Thumb;

@SuppressWarnings("serial")
public class ThumbPanel extends JPanel implements Scrollable {
	private MigLayout layout = new MigLayout(new LC().wrapAfter(4).fill()
			.align("center", "center"), new AC().fill().align("center").grow(),
			new AC().fill().align("center").grow());

	private List<Thumb> thumbs = new ArrayList<>();
	private List<Thumb> selectedThumbs = new ArrayList<>();

	private Border selectedThumbBorder = BorderFactory.createLineBorder(
			Color.cyan, 2);
	private Border currentThumbBorder = BorderFactory.createLineBorder(
			Color.blue, 2);

	private Border defaultThumbBorder = BorderFactory.createEmptyBorder(2, 2,
			2, 2);

	private Thumb currentThumb;

	public ThumbPanel() {
		super();
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

	public void libraryChanged() {
		this.thumbs.clear();
		this.selectedThumbs.clear();
		removeAll();
		repaint();
	}

	public void selectThumb(Thumb thumb) {
		JLabel label = thumb.getLabel();
		if (!layout.isManagingComponent(label))
			return;
		selectedThumbs.add(thumb);
		setBorder(thumb);
	}

	public void deselectThumb(Thumb thumb) {
		JLabel label = thumb.getLabel();
		if (!layout.isManagingComponent(label))
			return;
		selectedThumbs.remove(thumb);
		setBorder(thumb);
	}

	public void addThumb(Thumb thumb) {
		thumbs.add(thumb);
		JLabel label = thumb.getSmallThumb();
		setBorder(thumb);
		add(label);
	}

	public void setCurrentThumb(Thumb thumb) {
		JLabel label = thumb.getLabel();
		if (!layout.isManagingComponent(label))
			return;
		Thumb oldCurrentThumb = currentThumb;
		this.currentThumb = thumb;
		setBorder(currentThumb);
		setSize(currentThumb);
		if (oldCurrentThumb != null) {
			setBorder(oldCurrentThumb);
			setSize(oldCurrentThumb);
		}
	}
	
	private void setSize(Thumb thumb) {
		if (currentThumb != null && currentThumb.equals(thumb) && selectedThumbs.isEmpty()) {
			layout.setComponentConstraints(thumb.getLargeThumb(), "span 2 2");
		} else {
			layout.setComponentConstraints(thumb.getSmallThumb(), "span 1 1");
		}
	}
	
	private void setBorder(Thumb thumb) {
		if (currentThumb != null && currentThumb.equals(thumb)) {
			thumb.getLabel().setBorder(currentThumbBorder);
		} else if (selectedThumbs.contains(thumb)) {
			thumb.getLabel().setBorder(selectedThumbBorder);
		} else {
			thumb.getLabel().setBorder(defaultThumbBorder);
		}
	}
}
