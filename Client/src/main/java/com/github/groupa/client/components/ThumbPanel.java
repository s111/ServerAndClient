package com.github.groupa.client.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.views.GridView.Thumb;

@SuppressWarnings("serial")
public class ThumbPanel extends JPanel implements Scrollable {
	private MigLayout layout = new MigLayout("wrap 4");

	private Border selectedImageBorder = BorderFactory.createLineBorder(
			Color.blue, 2);

	private Border defaultImageBorder = BorderFactory.createEmptyBorder(2, 2,
			2, 2);

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
		removeAll();
	}

	public void selectThumb(Thumb thumb) {
		thumb.getLargeThumb().setBorder(selectedImageBorder);
	}

	public void deselectThumb(Thumb thumb) {
		thumb.getSmallThumb().setBorder(defaultImageBorder);
	}

	public void addThumb(Thumb thumb) {
		thumb.getLabel().setBorder(defaultImageBorder);
		Component component = thumb.getSmallThumb();
		add(component);
	}
}
