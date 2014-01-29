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
 *   Can use cache to generate smaller thumbnails
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
package com.github.groupa.client;

import java.util.HashMap;
import java.util.LinkedList;

public class ImageObject {
	public static final String ROTATE90 = "rotate90";
	public static final String ROTATE90CCW = "rotate90CCW";

	private ServerImageObject server;
	private String UniqueID;
	
	private LinkedList<String> localChanges = new LinkedList<>();
	
	private HashMap<String, Image> images = new HashMap<>();
	
	public void undoLastChange() {
		LinkedList<String> newChanges = localChanges;
		newChanges.removeLast();
		resetChanges();
		for (String change : newChanges) {
			modify(change);
		}
	}
	
	public void resetChanges() {
		localChanges = new LinkedList<>();
		images = new HashMap<>();
	}
	
	private void modify(String changes) {
		if (changes.compareTo(ROTATE90) == 0) {
			rotate90();
		}
	}

	/***
	 * Rotates image 90 degrees clockwise
	 */
	public void rotate90() {
		if (localChanges.getLast().equals(ROTATE90CCW)) {
			localChanges.removeLast();
		}
		else {
			addChange(ROTATE90);
		}
		for (Image image : images.values()) {
			//image.rotate90();
		}
	}
	
	/***
	 * Rotates image 90 degrees counter-clockwise
	 */
	public void rotate90CCW() {
		if (localChanges.getLast().equals(ROTATE90)) {
			localChanges.removeLast();
		}
		else {
			addChange(ROTATE90CCW);
		}
		for (Image image : images.values()) {
			//image.rotate90CCW();
		}
	}
	
	/***
	 * Adds change to the end of changelog
	 * @param change
	 */
	private void addChange(String change) {
		localChanges.add(change);
	}
	
}
