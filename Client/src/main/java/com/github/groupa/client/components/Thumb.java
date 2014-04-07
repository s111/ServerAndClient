package com.github.groupa.client.components;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;

public abstract class Thumb implements MouseListener {
	private ImageObject imageObject;
	private HashMap<String, JLabel> labels = new HashMap<String, JLabel>();
	private Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	public Thumb(ImageObject img) {
		this.imageObject = img;
	}

	public ImageObject getImageObject() {
		return imageObject;
	}

	public void setBorder(Border border) {
		this.border = border;
		for (JLabel label : labels.values()) {
			label.setBorder(border);
		}
	}

	public JLabel getThumb(String size) {
		JLabel label = labels.get(size);
		if (label == null) {
			final JLabel newLabel = new JLabel();
			newLabel.setText("Not loaded");
			label = newLabel;
			labels.put(size, label);
			label.addMouseListener(this);
			label.setBorder(border);
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

	public abstract void singleClick();

	public abstract void ctrlClick();

	public abstract void doubleClick();

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			if (arg0.getClickCount() == 1) {
				if (!arg0.isControlDown()) {
					singleClick();
				} else {
					ctrlClick();
				}
			} else if (arg0.getClickCount() == 2) {
				if (!arg0.isControlDown()) {
					doubleClick();
				}
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}