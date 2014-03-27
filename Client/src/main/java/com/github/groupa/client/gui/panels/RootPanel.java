package com.github.groupa.client.gui.panels;

import javax.swing.JPanel;

public class RootPanel implements Panel {
	JPanel panel = new JPanel();

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
