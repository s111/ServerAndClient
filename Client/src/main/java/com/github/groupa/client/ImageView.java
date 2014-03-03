package com.github.groupa.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.github.groupa.client.components.ImageDescriptionButton;
import com.github.groupa.client.components.ImageRater;

public class ImageView {
	private Library library = new Library();

	private JPanel mainPanel;

	private JLabel imageLabel = new JLabel("IMAGE");
	
	private JButton nextButton = new JButton("=>");
	private JButton previousButton = new JButton("<=");

	public ImageView() {
		setUpImageViewer();
		setImage(library.getNextImage());
	}

	private void setUpImageViewer() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		addPanelsToMainPanel();
		addButtonActionListeners();
		addKeyBindings();
	}

	@SuppressWarnings("serial")
	private void addKeyBindings() {
		InputMap inputMap = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = mainPanel.getActionMap();
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "keyLeft");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "keyRight");
		
		actionMap.put("keyLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayPreviousImage();
			}
		});
		
		actionMap.put("keyRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayNextImage();
			}
		});
	}

	private void addButtonActionListeners() {
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				displayNextImage();
			}
		});
		
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				displayPreviousImage();
			}
		});
	}

	public void displayNextImage() {
		setImage(library.getNextImage());
	}

	public void displayPreviousImage() {
		setImage(library.getPrevImage());
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		JLabel label = new JLabel("Top Toolbar");

		topPanel.add(label);

		return topPanel;
	}

	private JPanel createPicturePanel() {
		JPanel picturePanel = new JPanel();

		picturePanel.add(imageLabel);

		return picturePanel;
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.add(new ImageDescriptionButton(library).getButton());

		return leftPanel;
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();

		bottomPanel.add(previousButton);
		bottomPanel.add(new ImageRater(library).getPanel());
		bottomPanel.add(nextButton);

		return bottomPanel;
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel();
		JLabel label = new JLabel("Right Toolbar");

		rightPanel.add(label);

		return rightPanel;
	}

	private void addPanelsToMainPanel() {
		JPanel topPanel = createTopPanel();
		JPanel picturePanel = createPicturePanel();
		JPanel leftPanel = createLeftPanel();
		JPanel bottomPanel = createBottomPanel();
		JPanel rightPanel = createRightPanel();

		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(picturePanel, BorderLayout.CENTER);
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		mainPanel.add(rightPanel, BorderLayout.EAST);
	}

	private void setImage(ImageObject img) {
		if (img == null) {
			return;
		}

		imageLabel.setIcon(new ImageIcon(img.getImage()));
	}

	public JPanel getPanel() {
		return mainPanel;
	}
}
