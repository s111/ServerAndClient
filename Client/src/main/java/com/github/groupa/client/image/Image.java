package com.github.groupa.client.image;

/***
 * Stores actual image object as read from a file along with details on which format and such
 */
public class Image implements ImageManipulationInterface {

	@Override
	public void rotate90CW() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate90CCW() {
		// TODO Auto-generated method stub
		
	}
	
	public Image clone() {
		return this; //TODO: Return a new Image object
	}
}
