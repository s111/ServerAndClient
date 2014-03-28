package com.github.groupa.client.gui.panels;

import java.awt.CardLayout;

import javax.inject.Inject;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class MainPanel implements RootPanel {
	JPanel panel = new JPanel();

	private CardLayout sidebarPanelLayout;
	private CardLayout contentPanelLayout;

	private JPanel sidebarPanelContainer;
	private JPanel contentPanelContainer;

	private EventBus eventBus;

	@Inject
	public MainPanel(EventBus eventBus, JPanel sidebarPanelContainer,
			CardLayout sidebarPanelLayout, JPanel contentPanelContainer,
			CardLayout contentPanelLayout) {
		this.eventBus = eventBus;

		MigLayout layout = new MigLayout();
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

	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.hasSwitched()) {
			return;
		}

		String view = event.getView();

		if (view.equals(View.IMAGE_VIEW)) {
			switchSidebarPanel("imageSidebarPanel");
			switchContentPanel("imageContentPanel");
		} else {
			switchSidebarPanel("gridSidebarPanel");
			switchContentPanel("gridContentPanel");
		}

		eventBus.post(new SwitchViewEvent(event));
	}
}
