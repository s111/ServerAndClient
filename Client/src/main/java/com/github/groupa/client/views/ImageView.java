package com.github.groupa.client.views;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.components.ImageDescriptionButton;
import com.github.groupa.client.components.ImageRater;
import com.github.groupa.client.components.ImageTag;
import com.github.groupa.client.components.MetadataField;
import com.github.groupa.client.components.SearchField;
import com.github.groupa.client.events.DisplayedImageChangedEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ImageView {
	private Library library = null;
	private ImageObject activeImageObject = null;
	private int displayedImageIndex = 0;

	private JPanel mainPanel;
	private JPanel picturePanel;

	private JLabel imageLabel = new JLabel("image not loaded");

	private JButton nextButton = new JButton("=>");
	private JButton previousButton = new JButton("<=");
	private JButton previousViewButton = new JButton("<= Previous view");

	private EventBus eventBus;

	@Inject
	public ImageView(EventBus eventBus) {
		this.eventBus = eventBus;

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
		imageLabel.setText("");
		imageLabel.setIcon(null);
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
				updateImage(image);
				resizeToPanel();
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
				eventBus.post(new SwitchViewEvent(View.PREVIOUS));
			}
		});

	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.add(previousViewButton);
		topPanel.add(new SearchField(eventBus, this).getPanel());

		return topPanel;
	}

	private JPanel createPicturePanel() {
		picturePanel = new JPanel();
		picturePanel.add(imageLabel);

		picturePanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				resizeToPanel();
			}

		});

		picturePanel.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorRemoved(AncestorEvent event) {
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
				resizeToPanel();
			}
		});

		return picturePanel;
	}

	private JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(0, 1));
		leftPanel.add(new ImageDescriptionButton(this).getButton());
		leftPanel.add(new ImageTag(this).getTagButton());

		return leftPanel;
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		ImageRater imageRate = new ImageRater(this);
		bottomPanel.add(previousButton);
		bottomPanel.add(imageRate.getPanel());
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
		Border loweredetched = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder border = BorderFactory.createTitledBorder(loweredetched,
				title);

		pane.setBorder(border);
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

	private void resizeToPanel() {
		if (library == null || activeImageObject == null)
			return;

		Image image = activeImageObject.getImageRaw();

		int width = picturePanel.getWidth();
		int height = picturePanel.getHeight();

		if (image == null) {
			return;
		}

		BufferedImage resizedImage = resizeImage(image, width, height);
		updateImage(resizedImage);
	}

	private void updateImage(Image image) {
		imageLabel.setText("");
		imageLabel.setIcon(new ImageIcon(image));
	}

	private BufferedImage resizeImage(Image image, int width, int height) {
		final BufferedImage resizedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
}
