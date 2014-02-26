package com.github.groupa.client;

import java.awt.Component;
import java.awt.Container;
import java.io.IOException;

import javax.swing.JLabel;

import org.junit.Assert;
import org.junit.Test;

public class MainFrameTest {
	/*
	 * Cleanup
	 */
	@Test
	public void checkImageView() throws IOException {		
		MainFrame mainFrame = new MainFrame("App");
		
		mainFrame.setImageView(new ImageObject(1, mainFrame.loadImageFromDisk()));
		
		Container contentPane = mainFrame.getFrame().getContentPane();
		Component[] components = contentPane.getComponents();
		
		boolean found = false;
		
		for (Component component : components) {
			if (component instanceof JLabel) {
				Assert.assertNotNull("expected image", ((JLabel) component).getIcon());
				
				found = true;
			}
		}
		
		Assert.assertTrue("ImageView not found", found);
	}
}
