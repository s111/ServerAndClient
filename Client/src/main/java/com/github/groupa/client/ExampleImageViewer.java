package com.github.groupa.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class ExampleImageViewer {
	private Library library;
	private JPanel panel;
	private JPanel btnPanel;
	private JPanel imgPanel;
	private JButton prevBtn;
	private JButton nextBtn;
	private JLabel image;
	private ActionListener prevListener;
	private ActionListener nextListener;

	public ExampleImageViewer() {
		panel = new JPanel();
		imgPanel = new JPanel();
		btnPanel = new JPanel();
		image = new JLabel();
		setLibrary(new Library());
		createActionListeners();
		setupButtons();
		setupKeybinding();
		setupPanel();
		showImage(library.getNextImage());
	}

	public JPanel getPanel() {
		return panel;
	}

	private void setLibrary(Library library) {
		this.library = library;
	}

	private void createActionListeners() {
		prevListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showImage(library.getPrevImage());
			}
		};

		nextListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showImage(library.getNextImage());
			}
		};
	}

	private void showImage(ImageObject img) {
		if (img == null) {
			return;
		}
		image.setIcon(new ImageIcon(img.getImage()));
	}

	private void setupButtons() {
		btnPanel = new JPanel();
		prevBtn = new JButton("Prev");
		nextBtn = new JButton("Next");
		prevBtn.addActionListener(prevListener);
		nextBtn.addActionListener(nextListener);
		btnPanel.add(prevBtn);
		btnPanel.add(nextBtn);
	}
	
	@SuppressWarnings("serial")
	private void setupKeybinding() {
		imgPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "LeftAction");
		imgPanel.getActionMap().put("LeftAction", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showImage(library.getPrevImage());
			}
		});

		imgPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "RightAction");
		imgPanel.getActionMap().put("RightAction", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showImage(library.getNextImage());
			}
		});
	}

	private void setupPanel() {
		panel.setLayout(new BorderLayout());
		panel.add(btnPanel, BorderLayout.NORTH);

		imgPanel.setLayout(new BoxLayout(imgPanel, BoxLayout.X_AXIS));
		imgPanel.add(Box.createHorizontalGlue());
		imgPanel.add(image, BorderLayout.CENTER);
		imgPanel.add(Box.createHorizontalGlue());
		panel.add(imgPanel, BorderLayout.CENTER);
	}
}
