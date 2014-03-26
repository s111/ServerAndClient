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
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.components.ImageCropButton;
import com.github.groupa.client.components.ImageDescriptionButton;
import com.github.groupa.client.components.ImagePanel;
import com.github.groupa.client.components.ImageRater;
import com.github.groupa.client.components.ImageTag;
import com.github.groupa.client.components.MetadataField;
import com.github.groupa.client.components.RotateButtons;
import com.github.groupa.client.events.DisplayedImageChangedEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ImageView {
	private Library library = null;

	private ImageObject activeImageObject = null;

	private int displayedImageIndex = 0;

	private JPanel mainPanel;

	private JButton nextButton = new JButton("=>");
	private JButton previousButton = new JButton("<=");
	private JButton previousViewButton = new JButton("<= Previous view");

	private EventBus eventBus;

	private ImagePanel imagePanel;

	@Inject
	public ImageView(EventBus eventBus, SingleLibrary library) {
		this.eventBus = eventBus;
		this.library = library;
		eventBus.register(this);
		setUpImageViewer();
	}

	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.hasSwitched() && View.IMAGE_VIEW.equals(event.getView())) {
			ImageObject img = event.getImageObject();
			if (img != null) {
				Library lib = event.getLibrary();
				if (lib != null)
					library = lib;
				if (library == null)
					return;
				int idx = library.indexOf(img);
				if (idx < 0)
					return;
				displayedImageIndex = idx;
			}
			setImage(displayedImageIndex);
		}
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public ImageObject getActiveImageObject() {
		return activeImageObject;
	}

	public void setLibrary(Library library) {
		this.library = library;

		activeImageObject = null;

		displayedImageIndex = 0;
	}

	public Library getLibrary() {
		return library;
	}

	public void displayNextImage() {
		setImage(displayedImageIndex + 1);
	}

	public void displayPreviousImage() {
		setImage(displayedImageIndex - 1);
	}

	private void setImage(int idx) {
		if (library == null) {
			throw new IllegalStateException("Library not available");
		}

		int count = library.imageCount();

		if (count == 0) {
			return;
		}

		displayedImageIndex = (count + idx) % count;
		activeImageObject = library.getImage(displayedImageIndex);

		eventBus.post(new DisplayedImageChangedEvent(activeImageObject));

		activeImageObject.loadImageWithCallback(new Callback<Image>() {
			@Override
			public void success(Image image) {
				imagePanel.setImage(image);
			}

			@Override
			public void failure() {
			}
		});
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
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "previousView");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "previousView");

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
		actionMap.put("previousView", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventBus.post(new SwitchViewEvent(View.PREVIOUS));
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
				eventBus.post(new SwitchViewEvent(View.PREVIOUS));
			}
		});

	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel topRightPanel = new JPanel();
		JPanel topLeftPanel = new JPanel();

		topPanel.add(topRightPanel, BorderLayout.EAST);
		topPanel.add(topLeftPanel, BorderLayout.WEST);
		topLeftPanel.add(previousViewButton);

		return topPanel;
	}

	private JPanel createImagePanel() {
		imagePanel = new ImagePanel();

		eventBus.register(imagePanel);

		return imagePanel;
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(0, 1));
		leftPanel.add(new ImageDescriptionButton(this).getButton());
		leftPanel.add(new ImageTag(this).getTagButton());
		leftPanel.add(new ImageCropButton(this).getButton());
		setRaisedBevelBorder(leftPanel);

		return leftPanel;
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		ImageRater imageRate = new ImageRater(this);
		RotateButtons rotateButtons = new RotateButtons();

		bottomPanel.add(previousButton);
		bottomPanel.add(rotateButtons.getRotateCCWButton());
		bottomPanel.add(imageRate.getPanel());
		bottomPanel.add(rotateButtons.getRotateCWButton());
		bottomPanel.add(nextButton);

		eventBus.register(imageRate);
		return bottomPanel;
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel();
		MetadataField metadataField = new MetadataField(this);
		rightPanel.add(metadataField.getPanel());
		setTitledEtchedBorder("Metadata", rightPanel);

		eventBus.register(metadataField);
		return rightPanel;
	}

	private void setTitledEtchedBorder(String title, JPanel pane) {
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder border = BorderFactory.createTitledBorder(loweredetched, title);

		pane.setBorder(border);
	}

	private void setRaisedBevelBorder(JPanel pane) {
		Border raisedbevel;
		raisedbevel = BorderFactory.createRaisedBevelBorder();
		pane.setBorder(raisedbevel);
	}

	private void addPanelsToMainPanel() {
		JPanel topPanel = createTopPanel();
		JPanel imagePanel = createImagePanel();
		JPanel leftPanel = createLeftPanel();
		JPanel bottomPanel = createBottomPanel();
		JPanel rightPanel = createRightPanel();

		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(imagePanel, BorderLayout.CENTER);
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		mainPanel.add(rightPanel, BorderLayout.EAST);
	}
}