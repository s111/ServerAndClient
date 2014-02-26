package com.github.groupa.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class for showing next and/or previous image
 * 
 * @author Brynjulf Bent
 * 
 */

public class KeyboardListener implements KeyListener {

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			nextImage();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			previousImage();
		}
	}

	public int[] nextImage() {

		return null;
	}

	public int[] previousImage() {

		return null;
	}

}
