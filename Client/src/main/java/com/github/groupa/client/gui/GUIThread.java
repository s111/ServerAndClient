package com.github.groupa.client.gui;


public class GUIThread implements Runnable {
	private MainFrame mainFrame;

	public GUIThread(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void run() {
		mainFrame.display();
	}
}