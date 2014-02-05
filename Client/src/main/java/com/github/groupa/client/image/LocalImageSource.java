package com.github.groupa.client.image;

import java.io.File;
import java.util.LinkedList;

public class LocalImageSource extends ImageSource {
	private File mainImageFile;
	
	public LocalImageSource(File mainImageFile) {
		this.mainImageFile = mainImageFile;
	}
	
	@Override
	public boolean isSynchronized() {
		return changeLog.isEmpty();
	}
	
	@Override
	public void synchronize() {
		//TODO: Write MAIN_IMAGE
	}
	
	@Override
	public void synchronizeFrom(ImageSource src) {
		//TODO
	}
	
	@Override
	public void commit(LinkedList<String> changes) {
		super.commit(changes);
		changes.clear();
		synchronize(); // Locally stored images are always kept in sync
	}
	
	@Override
	protected void readImages() {
		// Read MAIN_IMAGE
	}

	@Override
	public void makeAvailable(String key) {
		if (images.get(key) == null) {
			Image image = new Image(mainImageFile);
			images.put(key, image.get(key));
		}
	}
}
