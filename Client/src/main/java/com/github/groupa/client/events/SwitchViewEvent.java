package com.github.groupa.client.events;

import java.util.List;

import com.github.groupa.client.ImageObject;

public class SwitchViewEvent {
	private String view;
	private boolean hasSwitched = false;
	private ImageObject imageObject = null;
	private List<ImageObject> imageList;
	
	public SwitchViewEvent(String view) {
		this.view = view;
	}

	public SwitchViewEvent(String view, ImageObject img) {
		this.view = view;
		this.imageObject = img;
	}
	
	public SwitchViewEvent(String view, ImageObject img, List<ImageObject> imageList) {
		this.view = view;
		this.imageObject = img;
		this.setImageList(imageList);
	}

	public SwitchViewEvent(SwitchViewEvent event) {
		setView(event.getView());
		setImageObject(event.getImageObject());
		setImageList(event.getImageList());
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

	public List<ImageObject> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageObject> imageList) {
		this.imageList = imageList;
	}
}
