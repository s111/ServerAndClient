package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;

public interface Requester {
	public Image requestImage(String href) throws IOException;
}
