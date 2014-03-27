package com.github.groupa.client.gui.panels;

import java.awt.CardLayout;

import javax.inject.Inject;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class RootPanel implements IRootPanel {
	JPanel panel = new JPanel();

	private CardLayout sidebarPanelLayout;
	private CardLayout contentPanelLayout;

	private JPanel sidebarPanelContainer;
	private JPanel contentPanelContainer;

	@Inject
	public RootPanel(JPanel sidebarPanelContainer,
			CardLayout sidebarPanelLayout, JPanel contentPanelContainer,
			CardLayout contentPanelLayout) {
		MigLayout layout = new MigLayout("debug");
		panel.setLayout(layout);

		this.sidebarPanelContainer = sidebarPanelContainer;
		this.contentPanelContainer = contentPanelContainer;

		this.sidebarPanelLayout = sidebarPanelLayout;
		this.contentPanelLayout = contentPanelLayout;

		sidebarPanelContainer.setLayout(sidebarPanelLayout);
		contentPanelContainer.setLayout(contentPanelLayout);

		panel.add(sidebarPanelContainer, "growy");
		panel.add(contentPanelContainer, "grow, push");
	}

	@Override
	public void addSidebarPanel(String identifier, SidebarPanel sidebarPanel) {
		sidebarPanelContainer.add(sidebarPanel.getPanel(), identifier);
	}

	@Override
	public void addContentPanel(String identifier, ContentPanel contentPanel) {
		contentPanelContainer.add(contentPanel.getPanel(), identifier);
	}

	@Override
	public void switchSidebarPanel(String identifier) {
		sidebarPanelLayout.show(sidebarPanelContainer, identifier);
	}

	@Override
	public void switchContentPanel(String identifier) {
		contentPanelLayout.show(contentPanelContainer, identifier);
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
}
