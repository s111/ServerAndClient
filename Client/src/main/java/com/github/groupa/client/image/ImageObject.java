package com.github.groupa.client.image;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.github.groupa.client.communication.ServerResponse;
import com.github.groupa.client.communication.ServerConnection;

/***
 * Instances should store:
 * ServerImageObject : This stores only what is on the server.
 *   For a simple refresh/fetch it will only contain pointers on how to get get the image
 *   Can store downloaded pictures in various sizes
 * Unique ID
 * Actual Image objects (Can be null if unavailable)
 * Changes done by client since last refresh. Used to generate server commands on Upload/Commit
 * Checksum or other form of version control (Timestamp?)
 * 
 * Instances should provide methods to:
 * Get a Image object in a specified dimension (Used by GUI) *BLOCKING OPERATION*
 * 
 * Make changes to images
 * 
 * Download changes from server (Additions to Refresh)
 *   Client modified && Server not modified 	-> Do nothing (Changes already visible in gui)
 *   Client modified && Server modified 		-> Alert GUI about the conflict
 *   Client not modified && Server not modified -> Do nothing
 *   Client not modified && Server modified		-> Automatically import changes from ServerImageObject
 *   
 * Upload changes to server (Commit)
 *   Client modified && Server not modified		-> Automatically export changes to ServerImageObject
 *   Client modified && Server modified			-> Alert GUI about the conflict
 *   Client not modified && Server not modified	-> Do nothing
 *   Client not modified && Server modified		-> Do nothing
 * 
 * Upload image to server
 * 
 * Reset locally done changes
 */
public class ImageObject implements ImageManipulationInterface {
	private static final long DEFAULT_UNIQUEID = -1l;
	
	private static final String ROTATE90CW = "rotate90CW";
	private static final String ROTATE90CCW = "rotate90CCW";

	public static final String MAIN_IMAGE = "Original";
	public static final String COMPRESSED_IMAGE = "Compressed";

	private transient long uniqueId = DEFAULT_UNIQUEID;
	private transient ServerImageObject serverImageObject = null;
	
	private LinkedList<String> changeLog = new LinkedList<>();
	private Map<String, Image> images = new HashMap<>();
	private Image localImage = null;
	
	public boolean isViewable() { return !images.isEmpty() || !serverImageObject.images.isEmpty(); }
	public boolean isUploaded() { return uniqueId != DEFAULT_UNIQUEID; }
	public boolean isModified() { return !changeLog.isEmpty(); }
	
	//TODO: Metadata ....

	public ImageObject(Image image) {
		uniqueId = DEFAULT_UNIQUEID;
		serverImageObject = null;
		localImage = image.clone();
		images.put(MAIN_IMAGE, image.clone());
	}
	
	public ImageObject(ServerResponse response) {
		uniqueId = response.uniqueId;
		serverImageObject = new ServerImageObject(response);
	}
	
	/***
	 * Rotates image 90 degrees clockwise
	 */
	public void rotate90CW() {
		if (changeLog.getLast().equals(ROTATE90CCW)) {
			changeLog.removeLast();
		}
		else {
			changeLog.add(ROTATE90CW);
		}
		modifyImages(images, ROTATE90CW);
	}
	
	/***
	 * Rotates image 90 degrees counter-clockwise
	 */
	public void rotate90CCW() {
		if (changeLog.getLast().equals(ROTATE90CW)) {
			changeLog.removeLast();
		}
		else {
			changeLog.add(ROTATE90CCW);
		}
		modifyImages(images, ROTATE90CCW);
	}
	
	/***
	 * Download changelog from server
	 */
	public void refresh() {
		if (serverImageObject == null) {
			//TODO: Can not run refresh when an image is not associated with an image on the server (Not yet uploaded)
			return;
		}
		ServerResponse result = ServerConnection.getInfo(uniqueId);
		if (!result.connectionSucceeded()) {
			//TODO: Notify GUI
			return;
		} else {
			if (serverImageObject.refresh(result)) {
				//TODO: Import changes from ServerImageObject
			} // No changes
		}
	}
	
	/***
	 * Commit changes and upload changelog to server
	 */
	public void commit() {
		if (serverImageObject == null) {
			//TODO: Can not commit changes to an image when it is not associated with an image on the server (Not yet uploaded)
			return;
		}
		if (changeLog.isEmpty()) return; // No changes to notify server about
		ServerResponse response = ServerConnection.commit(uniqueId, changeLog);
		if (!response.connectionSucceeded()) {
			//TODO: Notify GUI
			return;
		} else if (response.type != ServerResponse.Type.SUCCESS) {
			//TODO Check what went wrong
			throw new RuntimeException("Implementation error");
		} else { // SUCCESS
			//TODO: Export metadata to ServerImageObject
			for (String change : changeLog) {
				modifyImages(serverImageObject.images, change);
			}
			changeLog.clear();
		}
	}
	
	/***
	 * Upload a NEW image to the server
	 */
	public void upload() {
		if (serverImageObject != null) {
			//TODO: Don't upload an image when it already exists on server. Use commit instead
			throw new RuntimeException("Implementation error");
		}
		Image image;
		if ((image = images.get(MAIN_IMAGE)) == null) {
			// Don't try to upload an image if it is not the main image
			throw new RuntimeException("Implementation error");
		}
		ServerResponse response = ServerConnection.upload(image);
		if (!response.connectionSucceeded() || response.uniqueId == DEFAULT_UNIQUEID) {
			//TODO: Notify GUI
			return;
		} else if (response.type != ServerResponse.Type.SUCCESS) {
			//TODO Check what went wrong
			throw new RuntimeException("Implementation error");
		} else { // SUCCESS
			//TODO: Export metadata to ServerImageObject
			uniqueId = response.uniqueId;
			serverImageObject = new ServerImageObject(response);
			serverImageObject.images.put(MAIN_IMAGE, images.get(MAIN_IMAGE).clone());
			localImage = null;
		}
	}
	
	public void undoLastChange() {
		changeLog.removeLast();
		images.clear();
		if (serverImageObject == null) { // Local undo
			if (localImage != null) {
				images.put(MAIN_IMAGE, localImage.clone());
			} else {
				throw new RuntimeException("Implementation error");
			}
		}
		//TODO: Notify GUI
	}
	
	/***
	 * Reset all changes. This will clear image cache, so GUI should ask for a new one afterwards
	 */
	public void resetChanges() {
		changeLog.clear();
		images.clear();
	}

	private void modifyImages(Map<String, Image> images, String change) {
		if (ROTATE90CW.equals(change)) {
			for (Image image : images.values()) image.rotate90CW();
		}
		else if (ROTATE90CCW.equals(change)) {
			for (Image image : images.values()) image.rotate90CCW();
		}
		else throw new RuntimeException("Missing implementation");
	}
}
