package com.github.groupa.client.events;

import java.util.Set;

public class KnownTagsChangedEvent {
	private Set<String> tags;

	public KnownTagsChangedEvent(Set<String> tags) {
		this.setTags(tags);
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

}
