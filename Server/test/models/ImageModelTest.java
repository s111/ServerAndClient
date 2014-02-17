package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class ImageModelTest extends WithApplication {
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase()));
	}
	
	@Test
	public void createImageModelObject() {
		new ImageModel("../../images/01.png");
		
		ImageModel imageModel = ImageModel.find.where().eq("filename", "../../images/01.png").findUnique();
		
		assertNotNull("imageModel is null", imageModel);
		assertEquals("filename property doesn't match", "../../images/01.png", imageModel.filename);
	}
}
