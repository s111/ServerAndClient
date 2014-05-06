package com.github.groupa.client.components;

import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.github.groupa.client.BackgroundJob;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.ImageAvailableEvent;
import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.github.groupa.client.events.ImageModifiedEvent;
import com.google.common.eventbus.Subscribe;

public abstract class Thumb extends MouseAdapter implements AncestorListener {
	private ImageObject imageObject;
	private HashMap<String, JLabel> labels = new HashMap<String, JLabel>();
	private Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	private String toolTipText;
	private String lastSize;
	private JPanel rootPanel;

	public Thumb(ImageObject img, JPanel root) {
		this.imageObject = img;
		this.rootPanel = root;
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
		this.lastSize = size;
		JLabel label = labels.get(size);
		if (label == null) {
			label = createLabel(size);
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

	@Subscribe
	public void modifyImageListener(ImageModifiedEvent event) {
		ImageObject img = event.getImageObject();
		if (img.equals(imageObject)) {
			for (Map.Entry<String, JLabel> entry : labels.entrySet()) {
				JLabel label = entry.getValue();
				String size = entry.getKey();
				if (!setIcon(label, size)) {
					label.setIcon(null);
					imageObject.loadImage(size, BackgroundJob.HIGH_PRIORITY);
				}
			}
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

	@Subscribe
	public void imageAvailableListener(ImageAvailableEvent event) {
		ImageObject img = event.getImageObject();
		if (img.equals(imageObject)) {
			String size = event.getSize();
			JLabel label = labels.get(size);
			if (label == null) {
				label = createLabel(size);
				labels.put(size, label);
			} else {
				setIcon(label, size);
			}
		}
	}

	@Override
	public void ancestorAdded(AncestorEvent event) {
		checkLoad();
	}

	public void ancestorRemoved(AncestorEvent event) {
	}

	public void ancestorMoved(AncestorEvent event) {
		checkLoad();
	}

	private void checkLoad() {
		final JLabel label = labels.get(lastSize);
		if (label != null && label.getIcon() == null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (label.getBounds()
							.intersects(rootPanel.getVisibleRect())) {
						imageObject.loadImage(lastSize, BackgroundJob.MEDIUM_PRIORITY);
					}
				}
			});
		}
	}

	private JLabel createLabel(final String size) {
		@SuppressWarnings("serial")
		JLabel label = new JLabel() {
			@Override
			public Dimension getMinimumSize() {
				return getPreferredSize();
			}

			@Override
			public Dimension getMaximumSize() {
				return getPreferredSize();
			}

			@Override
			public Dimension getPreferredSize() {
				int s = ImageObject.thumbSize.get(size);
				return new Dimension(s, s);
			}
		};
		label.setText("Not loaded");
		setIcon(label, size);
		label.addMouseListener(this);
		label.addAncestorListener(this);
		label.setBorder(border);
		label.setToolTipText(toolTipText);
		return label;
	}

	private boolean setIcon(JLabel label, String size) {
		label.setHorizontalAlignment(SwingConstants.CENTER);
		BufferedImage img = imageObject.getThumb(size);
		if (img != null) {
			label.setIcon(new ImageIcon(img));
			label.setText(null);
			return true;
		} else
			return false;
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

		Date date = imageObject.getDateTaken();
		if (date != null) {
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
					DateFormat.SHORT, Locale.getDefault());
			toolTipText += "Creation date: " + df.format(date);
		}

		toolTipText += "</html>";
		return toolTipText;
	}
}
