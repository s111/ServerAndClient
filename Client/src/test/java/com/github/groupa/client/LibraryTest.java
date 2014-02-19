package com.github.groupa.client;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

public class LibraryTest {

	@Test
	public void test() {
		Image img1 = null, img2 = null;
		try {
			img1 = loadImageFromDisk("../../images/01.png");
			img2 = loadImageFromDisk("../../images/02.png");
		} catch (IOException e) {
			fail("Local images not found");
		}
		if (Library.size() != 0) {
			fail("Invalid count of images");
		}
		Library.add(1, img1);
		Library.add(2, img2);
		assertSame(img1, Library.get(1));
		assertSame(img2, Library.get(2));
		if (Library.size() != 2) {
			fail("Invalid count of images");
		}
	}
	
	private Image loadImageFromDisk(String path) throws IOException {
		return ImageIO.read(new File(path));
	}
}
