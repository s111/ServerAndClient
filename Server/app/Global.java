import java.io.File;

import models.ImageModel;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {
		File directory = new File("../../images");
		
		if (!directory.isDirectory()) return;
		
		File[] listOfFiles = directory.listFiles();
		
		for (File image : listOfFiles) {
			String filename = image.getName();
			
			if (filename.matches("^(.+).png$")) {
				ImageModel.create("../../images/" + filename);
			}
		}
	}
}