package com.github.groupa.client.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.MainFrame;
import com.github.groupa.client.components.ImageDescriptionButton;
import com.github.groupa.client.components.ImageRater;
import com.github.groupa.client.components.ImageTag;
import com.github.groupa.client.components.MetadataField;
import com.github.groupa.client.components.SearchField;
import com.github.groupa.client.events.ImageChangedEvent;
import com.github.groupa.client.events.ImageInfoChangedEvent;
import com.google.common.eventbus.EventBus;

public class ImageView {
	private Library library;

	private JPanel mainPanel;

	private JLabel imageLabel = new JLabel("image not loaded");

	private JButton nextButton = new JButton("=>");
	private JButton previousButton = new JButton("<=");
	private JButton previousViewButton = new JButton("<= Previous view");

	private MainFrame mainFrame;

	private EventBus eventBus;

	@Inject
	public ImageView(EventBus eventBus, MainFrame mainFrame, Library library) {
		this.eventBus = eventBus;
		this.mainFrame = mainFrame;
		this.library = library;

		setUpImageViewer();

		ImageObject image = library.getImage();
		setImage(image);
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
		InputMap inputMap = mainPanel
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
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

		previousViewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent action) {
				mainFrame.showLastView();
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

		topPanel.add(previousViewButton);
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
		leftPanel.setLayout(new GridLayout(0, 1));
		leftPanel.add(new ImageDescriptionButton(library).getButton());
		leftPanel.add(new ImageTag(library).getTagButton());

		return leftPanel;
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();

		ImageRater imageRate = new ImageRater(library);

		eventBus.register(imageRate);

		bottomPanel.add(previousButton);
		bottomPanel.add(imageRate.getPanel());
		bottomPanel.add(nextButton);

		return bottomPanel;
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel();

		MetadataField metadataField = new MetadataField();

		eventBus.register(metadataField);

		rightPanel.add(metadataField.getPanel());

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

		eventBus.post(new ImageChangedEvent());
		eventBus.post(new ImageInfoChangedEvent(img.getImageInfo()));

		img.loadImageWithCallback(new Callback<Image>() {
			@Override
			public void success(Image image) {
				imageLabel.setText("");
				imageLabel.setIcon(new ImageIcon(image));
			}

			@Override
			public void failure() {
			}
		});
	}

	public JPanel getPanel() {
		return mainPanel;
	}
}