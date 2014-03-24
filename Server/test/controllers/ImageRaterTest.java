package controllers;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.callAction;
import helpers.WithDatabase;

import java.util.HashMap;
import java.util.Map;

import models.Image;

import org.junit.Test;

import play.test.FakeRequest;
import queryDB.QueryImage;
import upload.Uploader;

public class ImageRaterTest extends WithDatabase {
	@Test
	public void rate_image_with_5_expect_rating_5() {
		String filename = Uploader.IMAGE_DIRECTORY + "01.png";

		Image image = new Image();
		image.setFilename(filename);

		QueryImage queryImage = new QueryImage(sessionFactory);
		queryImage.addImage(image);

		long id = image.getId();

		Map<String, String> data = new HashMap<>();
		data.put("value", "5");

		callAction(controllers.routes.ref.ImageRater.rate(id),
				new FakeRequest().withFormUrlEncodedBody(data));

		assertEquals(5, (int) queryImage.getImage(id).getRating());
	}
}
