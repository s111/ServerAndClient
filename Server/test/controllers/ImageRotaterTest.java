package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;
import helpers.WithDatabase;
import models.Image;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;
import queryDB.QueryImage;
import upload.Uploader;

public class ImageRotaterTest extends WithDatabase {
	@Before
	public void setUp() {
		super.setUp();

		String filename = Uploader.IMAGE_DIRECTORY + "01.png";

		Image image = new Image();
		image.setFilename(filename);

		QueryImage queryImage = new QueryImage(sessionFactory);
		queryImage.addImage(image);
	}

	@Test
	public void rotate_image_does_not_exist_expect_bad_request() {
		Result result = callAction(controllers.routes.ref.ImageRotater.rotate(
				2, 90));

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void rotate_image_with_bad_angle_expect_bad_request() {
		Result result = callAction(controllers.routes.ref.ImageRotater.rotate(
				1, 45));

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}
}
