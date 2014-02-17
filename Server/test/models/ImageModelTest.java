package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class ImageModelTest extends WithApplication {
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase()));
	}

	@Test
	public void addImageToDatabase() {
		String filename = "../../images/01.png";

		new ImageModel(filename).save();

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();

		assertNotNull("imageModel is null", imageModel);
		assertEquals("filename property doesn't match", filename,
				imageModel.filename);
	}

	@Test
	public void createAndSaveImageToDatabase() {
		String filename = "../../images/02.png";

		ImageModel.create(filename);

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();

		assertNotNull("imageModel is null", imageModel);
		assertEquals("filename property doesn't match", filename,
				imageModel.filename);
	}

	@Test
	public void getImageById() {
		String filename = "../../images/03.png";

		ImageModel.create(filename);

		ImageModel imageModel = ImageModel.getImageModel(1);

		assertNotNull("imageModel is null", imageModel);
		assertEquals("filename property doesn't match", filename,
				imageModel.filename);
	}

	@Test
	public void getAllImages() {
		List<String> filenames = new ArrayList<>();

		filenames.add("../../images/01.png");
		filenames.add("../../images/02.png");
		filenames.add("../../images/03.png");

		for (String filename : filenames)
			ImageModel.create(filename);

		List<ImageModel> imageModels = ImageModel.getAll();

		assertNotNull("ImageModel list is null", imageModels);
		assertEquals(
				"Number of Images in the database does not match the number of files",
				imageModels.size(), filenames.size());

		int numberOfImages = imageModels.size();

		for (int i = 0; i < numberOfImages; i++) {
			ImageModel imageModel = imageModels.get(i);

			assertNotNull("imageModel is null", imageModel);
			assertEquals("filename property doesn't match", filenames.get(i),
					imageModel.filename);
		}
	}
}
