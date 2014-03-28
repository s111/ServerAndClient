package com.github.groupa.client.gui;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import com.github.groupa.client.gui.frames.MainFrame;
import com.github.groupa.client.gui.panels.ContentPanel;
import com.github.groupa.client.gui.panels.GridContentPanel;
import com.github.groupa.client.gui.panels.GridSidebarPanel;
import com.github.groupa.client.gui.panels.RootPanel;
import com.github.groupa.client.gui.panels.ImageContentPanel;
import com.github.groupa.client.gui.panels.ImageSidebarPanel;
import com.github.groupa.client.gui.panels.SidebarPanel;
import com.google.common.eventbus.EventBus;

public class MainFrameTest {
	private RootPanel mockRootPanel;

	@Before
	public void setUp() {
		mockRootPanel = new RootPanel() {
			@Override
			public JPanel getPanel() {
				return new JPanel();
			}

			@Override
			public void switchSidebarPanel(String identifier) {
			}

			@Override
			public void switchContentPanel(String identifier) {
			}

			@Override
			public void addSidebarPanel(String identifier,
					SidebarPanel sidebarPanel) {
			}

			@Override
			public void addContentPanel(String identifier,
					ContentPanel contentPanel) {
			}
		};
	}

	@Test
	public void setTitle() {
		String title = "title";

		MainFrame mainFrame = createMainFrame();
		mainFrame.setTitle(title);

		assertEquals(title, mainFrame.getFrame().getTitle());
	}

	@Test
	public void setMinimumSize() {
		Dimension minimumSize = new Dimension(640, 480);

		MainFrame mainFrame = createMainFrame();
		mainFrame.setMinimumSize(minimumSize);

		assertEquals(minimumSize, mainFrame.getFrame().getMinimumSize());
	}

	private MainFrame createMainFrame() {
		MainFrame mainFrame = new MainFrame(new EventBus(), new JMenuBar(),
				mockRootPanel, mock(GridSidebarPanel.class),
				mock(GridContentPanel.class), mock(ImageSidebarPanel.class),
				mock(ImageContentPanel.class));
		return mainFrame;
	}
}