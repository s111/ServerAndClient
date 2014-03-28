package com.github.groupa.client.gui.panels;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.google.common.eventbus.EventBus;

public class GridContentPanel implements ContentPanel {
	private JPanel panel = new JPanel();

	@Inject
	public GridContentPanel(EventBus eventBus, GridPanel gridView) {
		MigLayout layout = new MigLayout();

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		eventBus.register(gridView);

		panel.add(gridView.getPanel(), "grow, push");
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
