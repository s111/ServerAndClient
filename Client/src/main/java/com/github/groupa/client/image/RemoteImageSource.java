package com.github.groupa.client.image;

import java.util.LinkedList;

import com.github.groupa.client.communication.HTTPConnection;
import com.github.groupa.client.communication.RemoteConnection;
import com.github.groupa.client.communication.ServerResponse;

/***
 * Should store everything about an image as received from server
 */
public class RemoteImageSource extends ImageSource {
	private RemoteConnection connection;
	private LinkedList<String> changeBackLog = new LinkedList<>();
	
	private final long uniqueId;
	
	/***
	 * 
	 * @param response
	 */
	public RemoteImageSource(ServerResponse response) {
		uniqueId = response.uniqueId;
	}
	
	public boolean isSynchronized() {
		return changeBackLog.isEmpty();
	}
	
	public void synchronize() {
		if (isSynchronized()) return;
		ServerResponse response = connection.commit(uniqueId, changeBackLog);
		if (!response.connectionSucceeded()) {
			//TODO: Notify GUI
			return;
		} else if (response.type != ServerResponse.Type.SUCCESS) {
			//TODO Check what went wrong
			throw new RuntimeException("Implementation error");
		} else { // SUCCESS
			changeBackLog.clear();
		}
	}
	
	@Override
	public void synchronizeFrom(ImageSource src) {
		Image image = src.getImage(MAIN_IMAGE);
		if (image == null) {
			throw new RuntimeException("Implementation error");
		}
		images.put(MAIN_IMAGE, image);
		ServerResponse response = connection.upload(uniqueId, image);
		//TODO
	}
	
	@Override
	public void makeAvailable(String key) {
		if (images.get(key) == null) {
			//TODO: Sizes ..
			ServerResponse response = connection.getImage(uniqueId, MAIN_IMAGE);
			if (!response.connectionSucceeded()) {
				//TODO
			} else if (response.type != ServerResponse.Type.SUCCESS) {
				//TODO
			} else { // SUCCESS
				Image image = new Image(response.getContent());
				images.put(key, image.get(key));
			}
		}
	}
	
	@Override
	public void commit(LinkedList<String> changes) {
		super.commit(changes);
		changeBackLog.addAll(changes);
	}
	
	/***
	 * @param response
	 * @return true if response indicates that there have been made changes
	 */
	public boolean refresh(ServerResponse response) {
		return false;
	}

	@Override
	protected void readImages() {
		// TODO Auto-generated method stub
		
	}

}
