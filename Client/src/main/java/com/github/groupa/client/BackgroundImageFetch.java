package com.github.groupa.client;

public class BackgroundImageFetch implements BackgroundJob {
	private ImageObject imageObject;
	private String size;
	private int priority = MEDIUM_PRIORITY;
	private Runnable runnable;

	public BackgroundImageFetch(Runnable runnable, ImageObject imageObject,
			String size) {
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

	public void run() {
		runnable.run();
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}
}
