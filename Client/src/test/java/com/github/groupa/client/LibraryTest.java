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

		assertEquals("Invalid count of images", 0, Library.size());

		imgObject1 = Library.add(new ImageObject(1, requester));
		lib = new Library();
		imgObject2 = Library.add(new ImageObject(2, requester));
		assertEquals("Invalid count of images", 2, Library.size());
		assertEquals("Invalid count of images", 2, lib.imageCount());

		assertSame(imgObject1, lib.getImage());
		assertSame(imgObject2, lib.getNextImage());
		assertSame(imgObject1, lib.getNextImage());
		assertSame(imgObject2, lib.getPrevImage());
		assertSame(imgObject1, lib.getPrevImage());
		assertSame(imgObject2, lib.getPrevImage());
		
		lib.addConstraint(Library.ConstraintType.HAS_IMAGE);
		assertEquals(0, lib.imageCount());
		imgObject1.getImage();
		imgObject2.getImage();
		ImageObject imgObject3 = new ImageObject(3, requester);
		Library.add(imgObject3);
		assertEquals(2, lib.imageCount()); // Last image should be unavailable
		lib.addConstraint(Library.ConstraintType.HAS_TAG,"tag");
		assertEquals(0, lib.imageCount());
		imgObject2.addTag("tag");
		assertEquals(0, lib.imageCount());
		lib.refresh();
		assertEquals(1,lib.imageCount());
		lib = new Library().addConstraint(Library.ConstraintType.HAS_IMAGE, true);
		assertEquals(2, lib.imageCount());
		lib.addConstraint(Library.ConstraintType.HAS_TAG, "tag");
		assertEquals(1, lib.imageCount());
		lib = new Library().addConstraint(Library.ConstraintType.HAS_IMAGE, false);
		assertEquals(1, lib.imageCount());
		// Will merge imageobject and library tests later.. This is just too messy.
	}
}
