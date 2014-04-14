package com.github.groupa.client.helpers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.util.List;

import com.github.groupa.client.ImageObject;

public class MockImageObject {
	private MockImageObject() {
	}

	public static ImageObject get(long id, String description, List<String> tags, BufferedImage image, int rating) {
		ImageObject img = mock(ImageObject.class);
		when(img.getId()).thenReturn(id);
		if (description != null) {
			when(img.getDescription()).thenReturn(description);
			when(img.hasDescription()).thenReturn(true);
		}
		if (tags != null) {
			when(img.getTags()).thenReturn(tags);
			for (String tag : tags) {
				when(img.hasTag(tag)).thenReturn(true);
			}
		}
		if (image != null) {
			when(img.getImageRaw()).thenReturn(image);
			when(img.getImage()).thenReturn(image);
		}
		when(img.getRating()).thenReturn(rating);

		return img;
	}

}
