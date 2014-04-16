package com.github.groupa.client.jsonobjects;

import java.util.List;

public class ImagesUpdate {
	private List<Long> ids;

	private ImageFull metadata;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public ImageFull getMetadata() {
		return metadata;
	}

	public void setMetadata(ImageFull metadata) {
		this.metadata = metadata;
	}
}