package com.github.groupa.client.main;

import javax.swing.SwingUtilities;

public class NewApplication {
	private Runnable guiThread;

	public NewApplication(Runnable guiThread) {
		this.guiThread = guiThread;
	}

	public void run() {
		SwingUtilities.invokeLater(guiThread);
	}
}
