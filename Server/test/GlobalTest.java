
import java.io.File;

import models.ImageModel;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class GlobalTest extends WithApplication {
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase()));
	}

	@Test
	public void checkInitialDatabase() {
		File directory = new File("../../images");

		File[] listOfFiles = directory.listFiles();
		
		int size = 0;
		
		for (File image : listOfFiles) {
			String filename = image.getName();
			
			if (filename.matches("^(.+).png$")) {
				size++;
			}
		}

		assertEquals("images in database does not match images on disk",
				ImageModel.getAll().size(), size);
	}
}
