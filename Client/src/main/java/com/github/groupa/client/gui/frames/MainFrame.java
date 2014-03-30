package com.github.groupa.client.gui.frames;

import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.gui.panels.ContentPanel;
import com.github.groupa.client.gui.panels.GridContentPanel;
import com.github.groupa.client.gui.panels.GridSidebarPanel;
import com.github.groupa.client.gui.panels.ImageContentPanel;
import com.github.groupa.client.gui.panels.ImageSidebarPanel;
import com.github.groupa.client.gui.panels.RootPanel;
import com.github.groupa.client.gui.panels.SidebarPanel;
import com.google.common.eventbus.EventBus;

public class MainFrame implements Frame {
	private static final Logger logger = LoggerFactory
			.getLogger(MainFrame.class);

	private static final String TITLE = "Photo Manager";

	private static final int MINIMUM_WIDTH = 640;
	private static final int MINIMUM_HEIGHT = 480;

	private EventBus eventBus;

	private JFrame frame = new JFrame();

	private RootPanel rootPanel;

	private SidebarPanel gridSidebarPanel;
	private ContentPanel gridContentPanel;
	private SidebarPanel imageSidebarPanel;
	private ContentPanel imageContentPanel;

	@Inject
	public MainFrame(EventBus eventBus, JMenuBar menuBar, RootPanel rootPanel,
			GridSidebarPanel gridSidebarPanel,
			GridContentPanel gridContentPanel,
			ImageSidebarPanel imageSidebarPanel,
			ImageContentPanel imageContentPanel) {
		this.eventBus = eventBus;
		this.rootPanel = rootPanel;
		this.gridSidebarPanel = gridSidebarPanel;
		this.gridContentPanel = gridContentPanel;
		this.imageSidebarPanel = imageSidebarPanel;
		this.imageContentPanel = imageContentPanel;

		eventBus.register(rootPanel);

		tryToSetSystemLookAndFeel();

		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(rootPanel.getPanel());

		setUpRootPanel();
	}

	@Override
	public JFrame getFrame() {
		return frame;
	}

	public void display() {
		frame.setVisible(true);
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public void setMinimumSize(Dimension minimumSize) {
		frame.setMinimumSize(minimumSize);
	}

	private void tryToSetSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			// If system look and feel can't be found, swing should just use the
			// crossplatform one
			logger.warn("Could not set system look and feel");
		}
	}

	private void setUpRootPanel() {
		eventBus.register(imageSidebarPanel);
		eventBus.register(imageContentPanel);

		rootPanel.addSidebarPanel("gridSidebarPanel", gridSidebarPanel);
		rootPanel.addContentPanel("gridContentPanel", gridContentPanel);
		rootPanel.addSidebarPanel("imageSidebarPanel", imageSidebarPanel);
		rootPanel.addContentPanel("imageContentPanel", imageContentPanel);
	}
}
