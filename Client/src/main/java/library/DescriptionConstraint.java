package library;

import com.github.groupa.client.ImageObject;

public class DescriptionConstraint extends LibraryConstraint {
	private boolean hasDescription;
	
	public DescriptionConstraint(boolean hasDescription) {
		this.hasDescription = hasDescription;
	}
	
	public boolean satisfied(ImageObject image) {
		return hasDescription == image.hasDescription();
	}
}
