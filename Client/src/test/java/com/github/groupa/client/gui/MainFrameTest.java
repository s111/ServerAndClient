package com.github.groupa.client.gui;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import com.github.groupa.client.gui.panels.ContentPanel;
import com.github.groupa.client.gui.panels.IRootPanel;
import com.github.groupa.client.gui.panels.ImageContentPanel;
import com.github.groupa.client.gui.panels.SidebarPanel;
import com.google.common.eventbus.EventBus;

public class MainFrameTest {
	private IRootPanel mockRootPanel;

	@Before
	public void setUp() {
		mockRootPanel = new IRootPanel() {
			@Override
			public JPanel getPanel() {
				return new JPanel();
			}

			@Override
			public void switchSidebarPanel(String identifier) {
				// TODO Auto-generated method stub

			}

			@Override
			public void switchContentPanel(String identifier) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addSidebarPanel(String identifier,
					SidebarPanel sidebarPanel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addContentPanel(String identifier,
					ContentPanel contentPanel) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Test
	public void setTitle() {
		String title = "title";

		MainFrame mainFrame = new MainFrame(new EventBus(), new JMenuBar(),
				mockRootPanel, mock(ImageContentPanel.class));
		mainFrame.setTitle(title);

		assertEquals(title, mainFrame.getFrame().getTitle());
	}

	@Test
	public void setMinimumSize() {
		Dimension minimumSize = new Dimension(640, 480);

		MainFrame mainFrame = new MainFrame(new EventBus(), new JMenuBar(),
				mockRootPanel, mock(ImageContentPanel.class));
		mainFrame.setMinimumSize(minimumSize);

		assertEquals(minimumSize, mainFrame.getFrame().getMinimumSize());
	}
}