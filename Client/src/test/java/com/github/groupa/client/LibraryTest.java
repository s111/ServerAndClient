package com.github.groupa.client;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

public class LibraryTest {

	@Test
	public void addLocalImagesToLibrary() {
		Image img1 = null, img2 = null;
		ImageObject imgObject1, imgObject2;
		Library lib;
		try {
			img1 = loadImageFromDisk("../../images/01.png");
			img2 = loadImageFromDisk("../../images/02.png");
		} catch (IOException e) {
			fail("Local images not found");
		}

		assertEquals("Invalid count of images", Library.size(), 0);
		
		imgObject1 = Library.add(1, img1);
		lib = new Library();
		assertNull("Nonexisting image returned",lib.getImage(2));
		imgObject2 = Library.add(2, img2);

		assertEquals("Invalid count of images", Library.size(), 2);
		
		assertSame(imgObject1, lib.getImage(1));
		assertSame(imgObject2, lib.getImage(2));
		assertSame(imgObject1, lib.getPrevImage());
		assertSame(imgObject2, lib.getNextImage());
		assertNull("Nonexisting image returned", lib.getNextImage());
	}

	private Image loadImageFromDisk(String path) throws IOException {
		return ImageIO.read(new File(path));
	}
}
