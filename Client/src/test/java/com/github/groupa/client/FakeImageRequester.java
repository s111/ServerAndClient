package com.github.groupa.client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;

public class FakeImageRequester implements Requester {
	
	private class FakeImage extends Image {
		@Override
		public Graphics getGraphics() {
			return null;
		}

		@Override
		public int getHeight(ImageObserver observer) {
			return 0;
		}

		@Override
		public Object getProperty(String name, ImageObserver observer) {
			return null;
		}

		@Override
		public ImageProducer getSource() {
			return null;
		}

		@Override
		public int getWidth(ImageObserver observer) {
			return 0;
		}
		
	}

	@Override
	public Image requestImage(long id) throws IOException {
		return new FakeImage();
	}
}
