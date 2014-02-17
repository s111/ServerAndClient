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
		String filename = "../../images/999.png";

		new ImageModel(filename).save();

		ImageModel imageModel = ImageModel.find.where()
				.eq("filename", filename).findUnique();

		assertNotNull("imageModel is null", imageModel);
		assertEquals("filename property doesn't match", filename,
				imageModel.filename);
	}

	@Test
	public void createAndSaveImageToDatabase() {
		String filename = "../../images/999.png";

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

		ImageModel createdModel = ImageModel.create(filename);

		ImageModel imageModel = ImageModel.getImageModel(createdModel.id);

		assertNotNull("imageModel is null", imageModel);
		assertEquals("filename property doesn't match", filename,
				imageModel.filename);
	}

	@Test
	public void getAllImages() {
		int initialSize = ImageModel.getAll().size();
		
		List<String> filenames = new ArrayList<>();

		filenames.add("../../images/901.png");
		filenames.add("../../images/902.png");
		filenames.add("../../images/903.png");

		for (String filename : filenames)
			ImageModel.create(filename);
		
		List<ImageModel> imageModels = ImageModel.getAll();
		
		int newSize = ImageModel.getAll().size();

		assertNotNull("ImageModel list is null", imageModels);
		assertEquals(
				"Number of Images in the database does not match the number of files",
				initialSize + 3, newSize);
	}
}
