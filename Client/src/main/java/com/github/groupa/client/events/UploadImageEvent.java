package com.github.groupa.client.events;

import java.io.File;

public class UploadImageEvent {
	private File file;
	
	public UploadImageEvent(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}
}
