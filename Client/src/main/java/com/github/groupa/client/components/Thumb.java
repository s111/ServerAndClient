package com.github.groupa.client.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.ImageModifiedEvent;
import com.google.common.eventbus.Subscribe;

public abstract class Thumb extends MouseAdapter {
	private ImageObject imageObject;
	private HashMap<String, JLabel> labels = new HashMap<String, JLabel>();
	private Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	private String toolTipText;

	public Thumb(ImageObject img) {
		this.imageObject = img;
		toolTipText = createToolTipText();
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
			label = new JLabel();
			label.setText("Not loaded");
			setIcon(label, size);
			label.addMouseListener(this);
			label.setBorder(border);
			label.setToolTipText(toolTipText);
			labels.put(size, label);
		}
		return label;
	}

	public abstract void singleClick();

	public abstract void ctrlClick();

	public abstract void doubleClick();

	public abstract void rightClick(MouseEvent arg0);

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
		} else {
			rightClick(arg0);
		}
	}

	private void refreshImages() {
		for (Map.Entry<String, JLabel> entry : labels.entrySet()) {
			setIcon(entry.getValue(), entry.getKey());
		}
	}

	@Subscribe
	public void modifyImageListener(ImageModifiedEvent event) {
		ImageObject img = event.getImageObject();
		if (img.equals(imageObject)) {
			refreshImages();
		}
	}

	@Subscribe
	public void imageChangeListener(ImageInfoChangedEvent event) {
		ImageObject img = event.getImageObject();
		if (!img.equals(imageObject))
			return;
		toolTipText = createToolTipText();
		for (JLabel label : labels.values()) {
			label.setToolTipText(toolTipText);
		}
	}

	private void setIcon(final JLabel label, String size) {
		BufferedImage img = imageObject.getThumb(size);
		if (img != null) {
			label.setIcon(new ImageIcon(img));
			label.setText(null);
		} else {
			imageObject.loadImage(new Callback<BufferedImage>() {
				@Override
				public void success(BufferedImage image) {
					label.setText(null);
					label.setIcon(new ImageIcon(image));
				}

				@Override
				public void failure() {
					label.setText("Error loading image");
				}
			}, size);
		}
	}

	private String createToolTipText() {
		String toolTipText = "<html>";
		String descr = imageObject.getDescription();
		if (descr != null)
			toolTipText += descr + "<br>";
		List<String> tags = imageObject.getTags();
		if (!tags.isEmpty()) {
			for (String tag : tags) {
				toolTipText += tag + "<br>";
			}
		}
		int rating = imageObject.getRating();
		if (rating != 0)
			toolTipText += "Rating: " + Integer.toString(rating) + "<br>";
		
		Date date = imageObject.getUploadDate();
		if (date != null) {
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
			toolTipText += "Uploaded: " + df.format(date);
		}
		
		toolTipText += "</html>";
		return toolTipText;
	}
}
