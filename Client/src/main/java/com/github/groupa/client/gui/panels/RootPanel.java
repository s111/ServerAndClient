package com.github.groupa.client.gui.panels;

import java.awt.CardLayout;

import javax.inject.Inject;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class RootPanel implements Panel {
	JPanel panel = new JPanel();

	private CardLayout sidebarPanelLayout;
	private CardLayout contentPanelLayout;

	private JPanel sidebarPanelContainer;
	private JPanel contentPanelContainer;

	@Inject
	public RootPanel(JPanel sidebarPanelContainer,
			CardLayout sidebarPanelLayout, JPanel contentPanelContainer,
			CardLayout contentPanelLayout) {
		MigLayout layout = new MigLayout();
		panel.setLayout(layout);

		this.sidebarPanelContainer = sidebarPanelContainer;
		this.contentPanelContainer = contentPanelContainer;

		this.sidebarPanelLayout = sidebarPanelLayout;
		this.contentPanelLayout = contentPanelLayout;

		sidebarPanelContainer.setLayout(sidebarPanelLayout);
		contentPanelContainer.setLayout(contentPanelLayout);

		panel.add(sidebarPanelContainer, "grow, wmin 20%");
		panel.add(contentPanelContainer, "grow, push, wmin 60%");
	}

	public void addSidebarPanel(String identifier, SidebarPanel sidebarPanel) {
		this.sidebarPanelContainer.add(sidebarPanel.getPanel(), identifier);
	}

	public void addContentPanel(String identifier, ContentPanel contentPanel) {
		this.contentPanelContainer.add(contentPanel.getPanel(), identifier);
	}

	public void switchSidebarPanel(String identifier) {
		sidebarPanelLayout.show(sidebarPanelContainer, identifier);
	}

	public void switchContentPanel(String identifier) {
		contentPanelLayout.show(contentPanelContainer, identifier);
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
