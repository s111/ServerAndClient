package com.github.groupa.client;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.groupa.client.servercommunication.Requester;

public class LibraryTest {

	@Test
	public void addLocalImagesToLibrary() {
		ImageObject imgObject1;
		ImageObject imgObject2;
		Library lib;
		Requester requester = new FakeRequester();

		assertEquals("Invalid count of images", Library.size(), 0);

		imgObject1 = Library.add(new ImageObject(1, requester));
		lib = new Library();
		imgObject2 = Library.add(new ImageObject(2, requester));
		assertEquals("Invalid count of images", Library.size(), 2);
		assertEquals("Invalid count of images", lib.imageCount(), 2);

		assertSame(imgObject1, lib.getImage());
		assertSame(imgObject2, lib.getNextImage());
		assertSame(imgObject1, lib.getNextImage());
		assertSame(imgObject2, lib.getPrevImage());
		assertSame(imgObject1, lib.getPrevImage());
		assertSame(imgObject2, lib.getPrevImage());
	}
}
