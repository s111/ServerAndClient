package com.github.groupa.client;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JLabel;

import org.junit.Assert;
import org.junit.Test;

public class MainFrameTest {
	@Test
	public void checkImageView() {		
		MainFrame mainFrame = new MainFrame("App");
		
		Container contentPane = mainFrame.getFrame().getContentPane();
		Component[] components = contentPane.getComponents();
		
		boolean found = false;
		
		for (Component component : components) {
			if (component instanceof JLabel) {
				Assert.assertFalse("expected image", ((JLabel) component).getIcon().equals(null));
				
				found = true;
			}
		}
		
		Assert.assertTrue("ImageView not found", found);
	}
}
