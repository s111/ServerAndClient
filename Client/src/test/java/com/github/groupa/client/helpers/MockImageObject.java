package com.github.groupa.client.helpers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.awt.Image;
import java.util.List;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.jsonobjects.ImageInfo;

public class MockImageObject {
	private MockImageObject() {
	}

	public static ImageObject get(long id, String description,
			ImageInfo imageInfo, List<String> tags, Image image, int rating) {
		ImageObject img = mock(ImageObject.class);
		when(img.getId()).thenReturn(id);
		if (description != null)
			when(img.getDescription()).thenReturn(description);
		if (imageInfo != null)
			when(img.getImageInfo()).thenReturn(imageInfo);
		if (tags != null) {
			when(img.getTags()).thenReturn(tags);
			for (String tag : tags) {
				when(img.hasTag(tag)).thenReturn(true);
			}
		}
		if (image != null)
			when(img.getImageRaw()).thenReturn(image);
		when(img.getRating()).thenReturn(rating);

		return img;
	}

}
