package com.github.groupa.client.gui.panels;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.google.common.eventbus.EventBus;

public class GridPanel implements Panel {
	private JPanel panel;
	private ThumbPanel thumbPanel;
	private String panelThumbSize = "m";
	private String previewThumbSize = "xl";

	@Inject
	public GridPanel(EventBus eventBus, ThumbPanel thumbPanel) {
		this.thumbPanel = thumbPanel;
		eventBus.register(this);
		setUpImageViewer();
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	public void setPanelThumbSize(String size) {
		panelThumbSize = size;
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

	private void setUpImageViewer() {
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
	}
}
