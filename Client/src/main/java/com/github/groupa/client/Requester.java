package com.github.groupa.client;

import java.awt.Image;
import java.io.IOException;

public interface Requester {
	public Image getImage(long id, String size) throws IOException;
}
