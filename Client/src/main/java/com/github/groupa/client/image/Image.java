package com.github.groupa.client.image;

/***
 * Stores actual image object as read from a file along with details on which format and such
 */
public class Image implements ImageManipulationInterface {
	String format = null;
	
	public Image(Object obj) {
		
	}
	@Override
	public void rotate90CW() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate90CCW() {
		// TODO Auto-generated method stub
		
	}
	
	public Image getImage(String img) {
		if (ImageSource.MAIN_IMAGE.equals(img)) {
			//TODO
		} else {
			
		}
		return null;
	}
	
	public Image clone() {
		return this; //TODO: Return a new Image object
	}
}
