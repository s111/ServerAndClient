package servercommunication;

import java.awt.Image;
import java.io.IOException;

import com.github.groupa.client.jsonobjects.ImageList;

public interface Requester {
	public Image getImage(long id, String size) throws IOException;
	
	public ImageList getImageList(int limit) throws IOException;
}
