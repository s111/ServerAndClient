package models;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class ModelTest extends WithApplication {
	@Before
	public void setup() {
		start(fakeApplication(inMemoryDatabase()));
	}

	@Test
	public void createAndRetrieveImage() {
		Date beforeImageIsSaved = new Date();

		ImageModel.create("/path/to/image/file.jpg");

		Date afterImageIsSaved = new Date();

		ImageModel image = ImageModel.find.where()
				.eq("filename", "/path/to/image/file.jpg").findUnique();

		assertThat(image).isNotNull();
		assertThat(image.filename).isEqualTo("/path/to/image/file.jpg");
		assertThat(image.added).isEqualTo(image.modified).isNotNull();

		assertTrue(image.added.after(beforeImageIsSaved)
				|| image.added.equals(beforeImageIsSaved));

		assertTrue(image.added.before(afterImageIsSaved)
				|| image.added.equals(afterImageIsSaved));
	}
}
