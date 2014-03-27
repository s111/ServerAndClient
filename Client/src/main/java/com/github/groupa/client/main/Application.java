package com.github.groupa.client.main;

import javax.swing.SwingUtilities;

public class Application {
	private Runnable guiThread;

	public Application(Runnable guiThread) {
		this.guiThread = guiThread;
	}

	public void run() {
		SwingUtilities.invokeLater(guiThread);
	}
}
