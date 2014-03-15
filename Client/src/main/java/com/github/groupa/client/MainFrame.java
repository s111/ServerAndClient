package com.github.groupa.client;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Stack;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.github.groupa.client.events.SwitchViewEvent;
import com.github.groupa.client.views.View;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class MainFrame {
	private JFrame frame;
	private Container contentPane;

	private CardLayout cardLayout;

	private EventBus eventBus;
	private Stack<String> views = new Stack<>();
	
	@Inject
	public MainFrame(EventBus eventBus) {
		this.eventBus = eventBus;
		this.eventBus.register(this);
		setUpMainFrame();
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public void setMenuBar(JMenuBar menuBar) {
		frame.setJMenuBar(menuBar);
	}

	private void setUpMainFrame() {
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cardLayout = new CardLayout();

		contentPane = frame.getContentPane();
		contentPane.setLayout(cardLayout);
	}

	public void display() {
		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void addView(Container content, String name) {
		contentPane.add(content, name);
	}

	public void showView(String name) {
		views.push(name);
		cardLayout.show(contentPane, name);
	}
	
	@Subscribe
	public void switchViewListener(SwitchViewEvent event) {
		if (event.hasSwitched()) return;
		SwitchViewEvent newEvent;
		String view = event.getView();
		if (View.PREVIOUS.equals(view)) {
			if (views.size() < 2) return;
			views.pop();
			view = views.peek();
			newEvent = new SwitchViewEvent(event);
			newEvent.setView(view);
		} else newEvent = new SwitchViewEvent(event);
		showView(view);
		eventBus.post(newEvent);
	}
}