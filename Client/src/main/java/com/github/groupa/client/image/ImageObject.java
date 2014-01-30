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
 * Metadata
 * Changes done by client since last refresh. Used to generate server commands on Upload/Commit
 * Changes done by server since last refresh. Used when there are conflicts.
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
	private static final String ROTATE90CW = "rotate90";
	private static final String ROTATE90CCW = "rotate90CCW";
	
	private static final String MAIN_IMAGE = "Original";

	private long uniqueId = -1l;
	private ServerImageObject serverImageObject = null;
	
	private LinkedList<String> changeLog = new LinkedList<>();
	private Map<String, Image> images = null;
	
	public boolean isViewable() { return images != null; }
	public boolean isUploaded() { return uniqueId != -1l; }
	
	//TODO: makeViewable()
	
	public void setUniqueId(long uuid) { this.uniqueId = uuid; }
	
	public ImageObject(Image image) {
		uniqueId = -1l;
		serverImageObject = null;
		images = new HashMap<String, Image>();
		images.put(MAIN_IMAGE, image);
	}
	
	public ImageObject(ServerResponse response) {
		uniqueId = response.uniqueId;
		serverImageObject = new ServerImageObject(response);
		
		//TODO: Import images from ServerImageObject, if there are any
	}
	
	/***
	 * Rotates image 90 degrees clockwise
	 */
	public void rotate90CW() {
		if (changeLog.getLast().equals(ROTATE90CCW)) {
			changeLog.removeLast();
		}
		else {
			addChange(ROTATE90CW);
		}
		if (images == null) return;
		for (Image image : images.values()) {
			image.rotate90CW();
		}
	}
	
	/***
	 * Rotates image 90 degrees counter-clockwise
	 */
	public void rotate90CCW() {
		if (changeLog.getLast().equals(ROTATE90CW)) {
			changeLog.removeLast();
		}
		else {
			addChange(ROTATE90CCW);
		}
		if (images == null) return;
		for (Image image : images.values()) {
			image.rotate90CCW();
		}
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
		if (!result.ok) {
			//TODO: Notify GUI
			return;
		} else {
			serverImageObject.refresh(result);
			//TODO: Import changes from ServerImageObject
		}
	}
	
	/***
	 * Upload changelog to server
	 */
	public void commit() {
		if (serverImageObject == null) {
			//TODO: Can not commit changes to an image when it is not associated with an image on the server (Not yet uploaded)
			return;
		}
		if (changeLog == null) return; // No changes to notify server about
		ServerResponse result = ServerConnection.commit(uniqueId, changeLog);
		if (!result.ok) {
			//TODO: Notify GUI
			return;
		} else {
			//TODO: Export changes to ServerImageObject
			changeLog = new LinkedList<>();
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
		if ((images == null) || ((image = images.get(MAIN_IMAGE))) == null) {
			// Don't try to upload an image if it does not exist
			throw new RuntimeException("Implementation error");
		}
		ServerResponse response = ServerConnection.upload(image);
		if (!response.ok){
			//TODO: Notify GUI
			return;
		} else {
			uniqueId = response.uniqueId;
			serverImageObject = new ServerImageObject(response);
		}
	}
	
	public void undoLastChange() {
		LinkedList<String> oldChangeLog = changeLog;
		oldChangeLog.removeLast();
		resetChanges();
		for (String change : oldChangeLog) {
			modify(change);
		}
	}
	
	public void resetChanges() {
		changeLog = new LinkedList<String>();
		images = null;
	}


	/***
	 * Adds change to the end of changelog
	 * @param change
	 */
	private void addChange(String change) {
		changeLog.add(change);
	}

	private void modify(String changes) {
		if (changes.compareTo(ROTATE90CW) == 0) {
			rotate90CW();
		}
		else if (changes.compareTo(ROTATE90CCW) == 0) {
			rotate90CCW();
		}
		else throw new RuntimeException("Missing implementation");
	}
}
