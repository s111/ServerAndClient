package com.github.groupa.client.gui;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import com.github.groupa.client.gui.panels.RootPanel;

public class MainFrameTest {
	private RootPanel mockRootPanel;

	@Before
	public void setUp() {
		JPanel panel = new JPanel();

		mockRootPanel = mock(RootPanel.class);
		when(mockRootPanel.getPanel()).thenReturn(panel);
	}

	@Test
	public void setTitle() {
		String title = "title";

		MainFrame mainFrame = new MainFrame(mockRootPanel, new JMenuBar());
		mainFrame.setTitle(title);

		assertEquals(title, mainFrame.getFrame().getTitle());
	}

	@Test
	public void setMinimumSize() {
		Dimension minimumSize = new Dimension(640, 480);

		MainFrame mainFrame = new MainFrame(mockRootPanel, new JMenuBar());
		mainFrame.setMinimumSize(minimumSize);

		assertEquals(minimumSize, mainFrame.getFrame().getMinimumSize());
	}
}