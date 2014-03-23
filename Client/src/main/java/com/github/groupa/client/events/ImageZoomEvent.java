package com.github.groupa.client.events;

public class ImageZoomEvent {
	private float zoom;

	public ImageZoomEvent(float zoom) {
		this.zoom = zoom;
	}

	public float getZoom() {
		return zoom;
	}
}
