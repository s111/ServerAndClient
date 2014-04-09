package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;
import helpers.WithDatabase;

import java.util.HashMap;
import java.util.Map;

import models.Image;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;
import queryDB.QueryImage;
import upload.Uploader;

public class ImageCropperTest extends WithDatabase {
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
	public void crop_image_does_not_exist_expect_bad_request() {
		Map<String, String> data = new HashMap<>();
		data.put("x", "0");
		data.put("y", "0");
		data.put("w", "128");
		data.put("h", "128");

		Result result = callAction(controllers.routes.ref.ImageCropper.crop(2),
				new FakeRequest().withFormUrlEncodedBody(data));

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void crop_image_with_bad_width_expect_bad_request() {
		Map<String, String> data = new HashMap<>();
		data.put("x", "0");
		data.put("y", "0");
		data.put("w", "0");
		data.put("h", "128");

		Result result = callAction(controllers.routes.ref.ImageCropper.crop(1),
				new FakeRequest().withFormUrlEncodedBody(data));

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}
}
