package com.github.groupa.client.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.components.Thumb;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.LibraryRemoveEvent;
import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.factories.ThumbMenuFactory;
import com.github.groupa.client.library.LibrarySort;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

@SuppressWarnings("serial")
public class ThumbPanel extends JPanel implements Scrollable {
	private GridLayout layout = new GridLayout(0, 2, 0, 0);

	private List<ImageObject> images = new ArrayList<>();
	private List<ImageObject> selectedImages = new ArrayList<>();

	private Map<ImageObject, Thumb> thumbs = new HashMap<>();

	private Border selectedThumbBorder = BorderFactory.createLineBorder(
			Color.blue, 2);
	private Border activeThumbBorder = BorderFactory.createLineBorder(
			Color.cyan, 2);

	private Border defaultThumbBorder = BorderFactory.createEmptyBorder(2, 2,
			2, 2);

	private String thumbSize = "m";

	private EventBus eventBus;
	private ImageObject activeImage = null;
	private Comparator<ImageObject> comparator = LibrarySort.SORT_ID_ASC;

	private int prevWidth = 333; // Hacky but it works (atm)

	@Inject
	public ThumbPanel(EventBus eventBus) {
		super();
		this.eventBus = eventBus;
		eventBus.register(this);
		setLayout(layout);
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "keyLeft");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "keyRight");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "keyUp");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "keyDown");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
		actionMap.put("enter", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				openSelectedImages(activeImage);
			}
		});
		actionMap.put("keyLeft", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				arrowKeyClicked(KeyEvent.VK_LEFT);
			}
		});
		actionMap.put("keyRight", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				arrowKeyClicked(KeyEvent.VK_RIGHT);
			}
		});
		actionMap.put("keyUp", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				arrowKeyClicked(KeyEvent.VK_UP);
			}
		});
		actionMap.put("keyDown", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				arrowKeyClicked(KeyEvent.VK_DOWN);
			}
		});

	}

	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(640, 480);
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if (orientation == SwingConstants.VERTICAL) {
			return 120;
		}
		return 0;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return 10;
	}

	public void widthChanged(int width) {
		if (images.isEmpty())
			return;
		this.prevWidth = width;
		setColumnCount(roomForColumns());
	}

	public List<ImageObject> getSelectedImages() {
		List<ImageObject> list = new ArrayList<>();
		list.addAll(selectedImages);
		if (activeImage != null && !list.contains(activeImage)) {
			list.add(activeImage);
		}
		return list;
	}

	public ImageObject getActiveImage() {
		return activeImage;
	}

	public void setActiveImage(ImageObject image) {
		if (image == activeImage || !images.contains(image))
			return;
		if (activeImage != null) {
			if (selectedImages.contains(activeImage)) {
				thumbs.get(activeImage).setBorder(selectedThumbBorder);
			} else {
				thumbs.get(activeImage).setBorder(defaultThumbBorder);
			}
		}
		activeImage = image;
		Thumb thumb = thumbs.get(activeImage);
		thumb.setBorder(activeThumbBorder);
		scrollRectToVisible(thumb.getThumb(thumbSize).getBounds());
	}

	public void setPanelThumbSize(String size) {
		if (this.thumbSize.equals(size))
			return;
		this.thumbSize = size;
		reAddThumbsToPanel();
		setColumnCount(roomForColumns());
	}

	@Subscribe
	public void libraryAddImageListener(LibraryAddEvent event) {
		ImageObject image = event.getImage();
		if (image == null) {
			addImages(event.getImages());
		} else {
			addImage(image);
		}
		sort();
	}

	@Subscribe
	public void libraryRemoveImageListener(LibraryRemoveEvent event) {
		ImageObject image = event.getImage();
		if (image == null) {
			removeImages(event.getImages());
		} else {
			removeImage(image);
		}
		sort();
	}

	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.getView() == View.GRID_VIEW) {
			requestFocus();
		}
	}

	private void arrowKeyClicked(int key) {
		if (activeImage == null)
			return;
		int imageNum = images.indexOf(activeImage);
		if (imageNum == -1)
			return;
		int imageCount = images.size();
		if (imageCount <= 1)
			return;
		int colCount = layout.getColumns();

		switch (key) {
		case KeyEvent.VK_LEFT: {
			if (imageNum > 0 && imageNum % colCount > 0) {
				selectSingleImage(images.get(imageNum - 1));
			}
			break;
		}
		case KeyEvent.VK_RIGHT: {
			if (imageNum < imageCount - 1 && imageNum % colCount < colCount - 1) {
				selectSingleImage(images.get(imageNum + 1));
			}
			break;
		}
		case KeyEvent.VK_DOWN: {
			if (imageNum == imageCount - 1) break;
			int wanted = imageNum + colCount;
			if (wanted >= imageCount) wanted = imageCount - 1;
			if (wanted < imageCount) {
				selectSingleImage(images.get(wanted));
			}
			break;
		}
		case KeyEvent.VK_UP: {
			int wanted = imageNum - colCount;
			if (wanted >= 0) {
				selectSingleImage(images.get(wanted));
			}
			break;
		}
		}
	}
	
	private void selectSingleImage(ImageObject image) {
		deselectImages();
		setActiveImage(image);
	}

	private int roomForColumns() {
		int size = ImageObject.thumbSize.get(this.thumbSize) + 4;
		int columns = (prevWidth - 20) / size;
		return columns;
	}

	private void setColumnCount(int columns) {
		if (images.isEmpty())
			return;
		if (columns == layout.getColumns())
			return;
		layout = new GridLayout(0, columns, 0, 0);
		setLayout(layout);
		revalidate();
		repaint();
	}

	private void reAddThumbsToPanel() {
		removeAll();
		for (ImageObject image : images) {
			Thumb thumb = thumbs.get(image);
			add(thumb.getThumb(thumbSize));
		}
		revalidate();
		repaint();
	}

	private void removeImages(List<ImageObject> list) {
		for (ImageObject img : list) {
			removeImage(img);
		}
		revalidate();
		repaint();
	}

	private void removeImage(ImageObject img) {
		if (images.remove(img)) {
			selectedImages.remove(img);
			if (activeImage == img)
				activeImage = null;
			remove(thumbs.get(img).getThumb(thumbSize));
		}
		revalidate();
		repaint();
	}

	private void addImages(List<ImageObject> list) {
		for (ImageObject img : list) {
			addImage(img);
		}
		revalidate();
		repaint();
	}

	private void addImage(final ImageObject image) {
		images.add(image);
		Thumb thumb = new Thumb(image, this) {
			@Override
			public void singleClick() {
				requestFocus();
				selectSingleImage(getImageObject());
			}

			@Override
			public void ctrlClick() {
				requestFocus();
				if (ThumbPanel.this.activeImage != null
						&& ThumbPanel.this.selectedImages.isEmpty()) {
					ThumbPanel.this.selectImage(ThumbPanel.this.activeImage);
				}
				if (ThumbPanel.this.selectedImages.contains(getImageObject())) {
					ThumbPanel.this.deselectImage(getImageObject());
				} else {
					ThumbPanel.this.selectImage(getImageObject());
				}
				ThumbPanel.this.setActiveImage(getImageObject());
			}

			@Override
			public void doubleClick() {
				requestFocus();
				eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW,
						getImageObject(), images));
			}

			@Override
			public void rightClick(MouseEvent event) {
				requestFocus();
				if (!ThumbPanel.this.selectedImages.contains(getImageObject())) {
					ThumbPanel.this.deselectImages();
					ThumbPanel.this.setActiveImage(getImageObject());
				}
				JPopupMenu menu = ThumbMenuFactory.getMenu(eventBus, image,
						getSelectedImages());
				menu.show(this.getThumb(thumbSize), event.getX(), event.getY());
			}
		};
		eventBus.register(thumb);
		thumbs.put(image, thumb);
		add(thumb.getThumb(thumbSize));
		revalidate();
		repaint();
	}

	private void openSelectedImages(ImageObject image) {
		eventBus.post(new SwitchViewEvent(View.IMAGE_VIEW, image,
				getSelectedImages()));
	}

	private void deselectImages() {
		List<ImageObject> list = new ArrayList<>();
		list.addAll(selectedImages);
		for (ImageObject image : list) {
			deselectImage(image);
		}
	}

	private void deselectImage(ImageObject image) {
		thumbs.get(image).setBorder(defaultThumbBorder);
		selectedImages.remove(image);
	}

	private void selectImage(ImageObject image) {
		thumbs.get(image).setBorder(selectedThumbBorder);
		selectedImages.add(image);
		if (comparator != null)
			Collections.sort(selectedImages, comparator);
	}

	public void sort(Comparator<ImageObject> comparator) {
		this.comparator = comparator;
		sort();
	}

	private void sort() {
		if (LibrarySort.sort(images, comparator)) {
			reAddThumbsToPanel();
		}
	}
}
