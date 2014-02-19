package com.github.groupa.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame {
	private static final Logger logger = LoggerFactory
			.getLogger(MainFrame.class);

	private JFrame frame;
	private Container contentPane;
	private JLabel imageView;

	private String title;

	public MainFrame(String title) {
		this.title = title;

		setUpMainFrame();
		addImageView();

		/*
		 * Commented out as we are trying to load a image from the server
		 * displayImage();
		 */
	}

	public void display() {
		frame.setVisible(true);
	}

	public void setImageView(ImageObject imageObject) {
		imageView.setIcon(new ImageIcon(imageObject.getImage()));
	}

	public JFrame getFrame() {
		return frame;
	}

	private void setUpMainFrame() {
		frame = new JFrame(title);
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = frame.getContentPane();
		// Just some cheap trick to get the image centered
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
	}

	private void addImageView() {
		imageView = new JLabel();

		// Just to keep the image centered
		contentPane.add(Box.createHorizontalGlue());

		contentPane.add(imageView, BorderLayout.CENTER);

		// Just to keep the image centered
		contentPane.add(Box.createHorizontalGlue());
	}

	@SuppressWarnings("unused")
	private void displayImage() {
		Image image = null;

		try {
			image = loadImageFromDisk();
		} catch (IOException exception) {
			logger.warn("Unable to load image");
		}

		if (image != null) {
			imageView.setIcon(new ImageIcon(image));
		}
	}

	private Image loadImageFromDisk() throws IOException {
		return ImageIO.read(new File("../../images/01.png"));
	}
}
