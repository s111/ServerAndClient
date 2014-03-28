package com.github.groupa.client.gui.panels;

public interface RootPanel extends Panel {
	public abstract void addSidebarPanel(String identifier,
			SidebarPanel sidebarPanel);

	public abstract void addContentPanel(String identifier,
			ContentPanel contentPanel);

	public abstract void switchSidebarPanel(String identifier);

	public abstract void switchContentPanel(String identifier);
}