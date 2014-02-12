package com.github.groupa.client.image;

import java.io.File;
import java.io.FileInputStream;
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
	public boolean synchronizeFrom(ImageSource src) {
		//TODO
		
		return false;
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
	public boolean makeAvailable(String resolution) { //TODO
		if (images.get(resolution) == null) {
			Image image = new Image(new FileInputStream(mainImageFile));
			if (image == null) {
				return false;
			}
			images.put(resolution, image.get(resolution));
		}
		return true;
	}
}
