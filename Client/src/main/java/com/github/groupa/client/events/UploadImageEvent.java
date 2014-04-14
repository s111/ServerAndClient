package com.github.groupa.client.events;

import java.io.File;

public class UploadImageEvent {
	private File[] files;
	
	public UploadImageEvent(File[] files) {
		this.files = files;
	}

	public File[] getFiles() {
		return files;
	}
}
