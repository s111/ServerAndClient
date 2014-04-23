package com.github.groupa.client.events;

import java.util.Comparator;

import com.github.groupa.client.ImageObject;

public class SwitchViewEvent {
	private String view;
	private boolean hasSwitched = false;
	private ImageObject imageObject = null;
	private Comparator<ImageObject> comparator = null;
	
	public SwitchViewEvent(String view) {
		this.view = view;
	}

	public SwitchViewEvent(String view, ImageObject img) {
		this.view = view;
		this.imageObject = img;
	}
	
	public SwitchViewEvent(String view, ImageObject img, Comparator<ImageObject> comparator) {
		this.view = view;
		this.imageObject = img;
		this.comparator = comparator;
	}

	public SwitchViewEvent(SwitchViewEvent event) {
		setView(event.getView());
		setImageObject(event.getImageObject());
		setComparator(event.getComparator());
		hasSwitched = true;
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

	public Comparator<ImageObject> getComparator() {
		return comparator ;
	}
	
	public void setComparator(Comparator<ImageObject> cmp) {
		comparator = cmp;
	}
}
