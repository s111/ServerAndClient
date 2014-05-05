package com.github.groupa.client;

import java.util.ArrayList;
import java.util.List;

public class BackgroundImageFetch<T> implements BackgroundJob<T> {
	private ImageObject imageObject;
	private String size;
	private int priority = MEDIUM_PRIORITY;
	private Runnable runnable;
	
	private List<Callback<T>> callbacks = new ArrayList<>();

	public BackgroundImageFetch(Runnable runnable, ImageObject imageObject, String size) {
		this.runnable = runnable;
		this.imageObject = imageObject;
		this.size = size;
	}

	public boolean equals(Object object) {
		if (object != null || !(object instanceof BackgroundImageFetch))
			return false;

		return hashCode() == object.hashCode();
	}
	
	public int hashCode() {
		return imageObject.hashCode() * 33 + size.hashCode();
	}
	
	public String toString() {
		return size + imageObject.getId();
	}

	public void start() {
		runnable.run();
	}

	@SuppressWarnings("unchecked")
	public void run() {
		runnable.run();
		if (imageObject.hasImage(size)) {
			for (Callback<T> callback : callbacks) {
				callback.success((T)imageObject.getImage(size));
			}
		} else {
			for (Callback<T> callback : callbacks) {
				callback.failure();
			}
		}
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	public void addCallback(Callback<T> callback) {
		callbacks.add(callback);
	}
}
