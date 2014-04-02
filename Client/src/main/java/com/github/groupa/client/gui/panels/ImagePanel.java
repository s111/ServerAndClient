package com.github.groupa.client.gui.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import com.github.groupa.client.gui.listeners.ImagePanelMouseListener;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	private Image image;

	private ImagePanelMouseListener imagePanelMouseListener;

	public ImagePanel() {
		imagePanelMouseListener = new ImagePanelMouseListener(this);

		addMouseListener(imagePanelMouseListener);
		addMouseMotionListener(imagePanelMouseListener);
		addMouseWheelListener(imagePanelMouseListener);
	}

	public void setImage(Image image) {
		this.image = image;

		imagePanelMouseListener.resetZoom();
		imagePanelMouseListener.resetPan();

		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		setOpaque(false);
		Graphics2D g2d = (Graphics2D) g.create();

		if (image == null) {
			return;
		}

		g2d.drawImage(image, imagePanelMouseListener.getCurrentTransform(),
				null);
		g2d.dispose();
	}

	public Image getImage() {
		return image;
	}
}
