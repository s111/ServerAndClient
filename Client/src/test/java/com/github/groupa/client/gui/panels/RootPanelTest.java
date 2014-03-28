package com.github.groupa.client.gui.panels;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.CardLayout;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import com.google.common.eventbus.EventBus;

public class RootPanelTest {
	private JPanel sidebarPanelContainer;
	private SidebarPanel mockSidebarPanel1;
	private SidebarPanel mockSidebarPanel2;

	private JPanel contentPanelContainer;
	private ContentPanel mockContentPanel1;
	private ContentPanel mockContentPanel2;

	private CardLayout sidebarPanelLayout;
	private CardLayout contentPanelLayout;

	private RootPanel rootPanel;

	@Before
	public void setUp() {
		createMocks();
		setUpRootPanel();
	}

	private void createMocks() {
		JPanel sidebarPanel1 = new JPanel();
		JPanel sidebarPanel2 = new JPanel();
		JPanel contentPanel1 = new JPanel();
		JPanel contentPanel2 = new JPanel();

		mockSidebarPanel1 = mock(SidebarPanel.class);
		when(mockSidebarPanel1.getPanel()).thenReturn(sidebarPanel1);

		mockSidebarPanel2 = mock(SidebarPanel.class);
		when(mockSidebarPanel2.getPanel()).thenReturn(sidebarPanel2);

		mockContentPanel1 = mock(ContentPanel.class);
		when(mockContentPanel1.getPanel()).thenReturn(contentPanel1);

		mockContentPanel2 = mock(ContentPanel.class);
		when(mockContentPanel2.getPanel()).thenReturn(contentPanel2);
	}

	private void setUpRootPanel() {
		sidebarPanelContainer = new JPanel();
		contentPanelContainer = new JPanel();

		sidebarPanelLayout = new CardLayout();
		contentPanelLayout = new CardLayout();

		rootPanel = new MainPanel(new EventBus(), sidebarPanelContainer,
				sidebarPanelLayout, contentPanelContainer, contentPanelLayout);
	}

	@Test
	public void addSidebarPanel() {
		rootPanel.addSidebarPanel("sidebarPanel1", mockSidebarPanel1);

		assertTrue(mockSidebarPanel1.getPanel().getParent() == sidebarPanelContainer);
	}

	@Test
	public void addContentPanel() {
		rootPanel.addContentPanel("contentPanel1", mockContentPanel1);

		assertTrue(mockContentPanel1.getPanel().getParent() == contentPanelContainer);
	}

	@Test
	public void switchSidebarPanel() {
		rootPanel.addSidebarPanel("sidebarPanel1", mockSidebarPanel1);
		rootPanel.addSidebarPanel("sidebarPanel2", mockSidebarPanel2);

		rootPanel.switchSidebarPanel("sidebarPanel2");

		assertFalse(mockSidebarPanel1.getPanel().isVisible());
		assertTrue(mockSidebarPanel2.getPanel().isVisible());
	}

	@Test
	public void switchContentPanel() {
		rootPanel.addContentPanel("contentPanel1", mockContentPanel1);
		rootPanel.addContentPanel("contentPanel2", mockContentPanel2);

		rootPanel.switchContentPanel("contentPanel2");

		assertFalse(mockContentPanel1.getPanel().isVisible());
		assertTrue(mockContentPanel2.getPanel().isVisible());
	}
}
