package com.github.groupa.client.gui.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.components.ZoomSlider;
import com.github.groupa.client.library.LibrarySort;

public class GridSidebarPanel implements SidebarPanel {
	private JPanel panel = new JPanel();

	private RatingFilterPanel ratingFilterPanel;
	private ThumbPanel thumbPanel;
	private ZoomSlider zoomSlider;

	private EditMetadataWindow editMetadataWindow;

	private TagFilterPanel tagFilterPanel;

	@Inject
	public GridSidebarPanel(ThumbPanel thumbPanel, ZoomSlider zoomSlider,
			EditMetadataWindow editMetadataWindow,
			RatingFilterPanel ratingPanel, TagFilterPanel tagFilterPanel) {
		this.thumbPanel = thumbPanel;
		this.zoomSlider = zoomSlider;
		this.ratingFilterPanel = ratingPanel;
		this.editMetadataWindow = editMetadataWindow;
		this.tagFilterPanel = tagFilterPanel;

		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		setUpSortComponents();
		setUpTagComponents();
		setUpRatingFilterPanel();
		setUpZoomComponents();
		setUpEditMetadataComponent();
	}

	private void setUpRatingFilterPanel() {
		panel.add(ratingFilterPanel.getPanel(), "align center, wrap");
	}

	private void setUpZoomComponents() {
		JLabel thumbSizeLabel = new JLabel("Thumbnail size");
		thumbSizeLabel.setFont(new Font(thumbSizeLabel.getFont().getName(),
				Font.BOLD, 13));

		JPanel zoomPanel = new JPanel();
		zoomPanel.setLayout(new MigLayout());
		zoomPanel.add(thumbSizeLabel, "wrap");
		zoomPanel.add(zoomSlider.getPanel(), "align center");

		panel.add(zoomPanel, "wrap");
	}

	private void setUpTagComponents() {
		panel.add(tagFilterPanel.getPanel(), "grow, push, wrap");
	}

	private void setUpSortComponents() {
		String[] sortTypes = { "ID", "Rating (desc)", "Rating (asc)",
				"Creation date (asc)", "Creation date (desc)" };

		final JComboBox<String> comboBox = new JComboBox<>(sortTypes);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (comboBox.getSelectedIndex() == 0) {
							thumbPanel.sort(LibrarySort.SORT_ID_ASC);
						} else if (comboBox.getSelectedIndex() == 1) {
							thumbPanel.sort(LibrarySort.SORT_RATING_DESC);
						} else if (comboBox.getSelectedIndex() == 2) {
							thumbPanel.sort(LibrarySort.SORT_RATING_ASC);
						} else if (comboBox.getSelectedIndex() == 3) {
							thumbPanel.sort(LibrarySort.SORT_UPLOAD_DATE_ASC);
						} else if (comboBox.getSelectedIndex() == 4) {
							thumbPanel.sort(LibrarySort.SORT_UPLOAD_DATE_DESC);
						}
					}
				});
			}
		});

		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new MigLayout());
		JLabel sortLabel = new JLabel("Sort");
		sortLabel
				.setFont(new Font(sortLabel.getFont().getName(), Font.BOLD, 13));
		comboPanel.add(sortLabel, "wrap");
		comboPanel.add(comboBox, "width 128, wrap");

		panel.add(comboPanel, "wrap");
	}

	private void setUpEditMetadataComponent() {
		JButton editMetadataButton;

		editMetadataButton = new JButton("Edit selected images");

		editMetadataButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				editMetadataWindow.display();
			}
		});

		panel.add(editMetadataButton, "align right");
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
