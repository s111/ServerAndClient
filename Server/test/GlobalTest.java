import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;

import java.io.File;

import models.ImageModel;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;
import upload.Uploader;

public class GlobalTest extends WithApplication {
	@Before
	public void startApp() {
		start(fakeApplication());
	}

	@Test
	public void check_initial_database_size_expect_to_match_files_on_disk() {
		File directory = new File(Uploader.IMAGE_DIRECTORY);

		File[] listOfFiles = directory.listFiles();

		if (listOfFiles == null)
			return;

		int size = 0;

		for (File image : listOfFiles) {
			String filename = image.getName();

			if (filename.matches("^(.+).(png|jpg)$")) {
				if (filename.contains("thumb"))
					continue;
				size++;
			}
		}

		assertEquals("images in database does not match images on disk",
				ImageModel.getAll().size(), size);
	}
}
