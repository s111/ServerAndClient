package com.github.groupa.client.events;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;

public class SwitchViewEvent {
	private String view;
	private boolean hasSwitched = false;
	private ImageObject imageObject = null;
	private Library library = null;
	
	public SwitchViewEvent(String view) {
		this.view = view;
	}

	public SwitchViewEvent(String view, ImageObject img) {
		this.view = view;
		this.imageObject = img;
	}
	
	public SwitchViewEvent(SwitchViewEvent event) {
		setView(event.getView());
		setImageObject(event.getImageObject());
		setLibrary(event.getLibrary());
		hasSwitched = true;
	}
	
	public SwitchViewEvent(String view, ImageObject img, Library library) {
		this.view = view;
		this.imageObject = img;
		this.library = library;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public String getView() {
		return view;
	}
	
	public boolean hasSwitched() {
		return hasSwitched;
	}
	
	public ImageObject getImageObject() {
		return imageObject;
	}
	
	public void setImageObject(ImageObject img) {
		imageObject = img;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}
}
