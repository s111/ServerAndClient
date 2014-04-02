package com.github.groupa.client.gui.panels;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.ImageObject;

public class GridContentPanel implements ContentPanel {
	private JPanel panel = new JPanel();
	private ThumbPanel thumbPanel;
	private String panelThumbSize = "m";
	private String previewThumbSize = "xl";

	@Inject
	public GridContentPanel(ThumbPanel thumbPanel) {
		this.thumbPanel = thumbPanel;
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		panel.add(getThumbPanel(), "grow, push");
	}

	public void setPanelThumbSize(String size) {
		thumbPanel.setPanelThumbSize(size);
	}

	public void setPreviewThumbSize(String size) {
		previewThumbSize = size;
	}

	public String getPreviewThumbSize() {
		return previewThumbSize;
	}

	public String getPanelThumbSize() {
		return panelThumbSize;
	}

	public List<ImageObject> getSelectedImages() {
		return thumbPanel.getSelectedImages();
	}

	public ImageObject getActiveImage() {
		return thumbPanel.getActiveImage();
	}

	public void setActiveImage(ImageObject image) {
		thumbPanel.setActiveImage(image);
	}

	private JPanel getThumbPanel() {
		MigLayout layout = new MigLayout("fill");
		panel = new JPanel(layout);
		final JScrollPane thumbScroll = new JScrollPane(thumbPanel);
		thumbScroll.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				thumbPanel.widthChanged(thumbScroll.getWidth());
			}
		});
		panel.add(thumbScroll, "grow");
		return panel;
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
