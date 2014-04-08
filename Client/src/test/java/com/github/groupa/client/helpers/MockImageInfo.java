package com.github.groupa.client.helpers;

import com.github.groupa.client.jsonobjects.ImageFull;
import com.github.groupa.client.jsonobjects.ImageInfo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockImageInfo {
	public static ImageInfo get(ImageFull imageFull) {
		ImageInfo info = mock(ImageInfo.class);
		when(info.getImage()).thenReturn(imageFull);
		return info;
	}
}
