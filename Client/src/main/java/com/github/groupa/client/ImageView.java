package com.github.groupa.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import com.github.groupa.client.components.MetadataField;
import com.github.groupa.client.components.SearchField;

public class ImageView {
	private Library library = new Library();
	
	private JPanel mainPanel;

	private JLabel imageLabel = new JLabel();
	
	private JButton nextButton = new JButton("=>");
	private JButton previousButton = new JButton("<=");
	private JButton backToMainButton = new JButton("<= Exit to Main");
	private JButton tagImageButton = new JButton("Tag Picture");

	public ImageView() {
		setUpImageViewer();
		setImage(library.getImage());
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
		
		backToMainButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				// Call change view method on App
			}
		});
		
		tagImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				// Tag picture and put in metadata
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
		
		topPanel.add(backToMainButton);
		topPanel.add(new SearchField().getPanel());

		return topPanel;
	}

	private JPanel createPicturePanel() {
		JPanel picturePanel = new JPanel();

		picturePanel.add(imageLabel);

		return picturePanel;
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(0,1));
		leftPanel.add(new ImageDescriptionButton(library).getButton());
		leftPanel.add(tagImageButton);

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
		rightPanel.add(new MetadataField().getPanel());

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
