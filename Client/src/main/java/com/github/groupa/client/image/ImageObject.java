/***
 * Instances should store:
 * ServerImageObject : This stores only what is on the server.
 *   For a simple refresh/fetch it will only contain pointers on how to get get the image
 *   Can store downloaded pictures in various sizes
 * Unique ID
 * Actual picture objects (Can be null if unavailable)
 * Metadata
 * Changes done by client since last refresh. Used to generate server commands on Upload/Commit
 * Changes done by server since last refresh. Used when there are conflicts.
 * Checksum or other form of version control (Timestamp?)
 * 
 * Instances should provide methods to:
 * Get a picture object in a specified dimension (Used by GUI) *BLOCKING OPERATION*
 * 
 * Download changes from server (Refresh)
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
 * Reset locally done changes
 * 
 * 
 */

package com.github.groupa.client.image;

import java.util.HashMap;
import java.util.LinkedList;

import com.github.groupa.client.communication.Result;
import com.github.groupa.client.communication.ServerConnection;

public class ImageObject {
	public static final String ROTATE90 = "rotate90";
	public static final String ROTATE90CCW = "rotate90CCW";

	private ServerImageObject server;
	public long UniqueID = -1l;
		
	private LinkedList<String> changeLog = new LinkedList<>();
	private HashMap<String, Image> images = null;
	
	public boolean isViewable() { return images != null; }
	
	public boolean isUploaded() { return UniqueID != -1l; }
	
	/***
	 * Rotates image 90 degrees clockwise
	 */
	public void rotate90() {
		if (changeLog.getLast().equals(ROTATE90CCW)) {
			changeLog.removeLast();
		}
		else {
			addChange(ROTATE90);
		}
		if (images == null) return;
		for (Image image : images.values()) {
			//image.rotate90();
		}
	}
	
	/***
	 * Rotates image 90 degrees counter-clockwise
	 */
	public void rotate90CCW() {
		if (changeLog.getLast().equals(ROTATE90)) {
			changeLog.removeLast();
		}
		else {
			addChange(ROTATE90CCW);
		}
		if (images == null) return;
		for (Image image : images.values()) {
			//image.rotate90CCW();
		}
	}
	
	/***
	 * Download changelog from server
	 */
	public void refresh() {
		Result result = ServerConnection.getInfo(this);
		if (!result.ok) {
			//TODO: Notify GUI
			return;
		} else {
			//TODO: Import changes from ServerImageObject
		}
	}
	
	/***
	 * Upload changelog to server
	 */
	public void commit() {
		if (changeLog == null) return;
		Result result = ServerConnection.commit(this);
		if (!result.ok) {
			//TODO: Notify GUI
			return;
		} else {
			//TODO: Export changes to ServerImageObject
			changeLog = new LinkedList<>();
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
		if (changes.compareTo(ROTATE90) == 0) {
			rotate90();
		}
	}
}
