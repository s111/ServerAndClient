package controllers;

import static helpers.ResultHelper.isOK;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeRequest;
import helpers.WithDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Image;
import models.Tag;

import org.junit.Test;

import play.mvc.Result;
import queryDB.QueryImage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageUpdateMultipleTest extends WithDatabase {
	private static final String JSON_VALID = "{\"images\":[{\"id\":1,\"description\":\"asd\",\"rating\":5,\"tags\":[\"tag1\",\"tag2\"]},{\"id\":2,\"description\":\"fgh\",\"rating\":3,\"tags\":[\"tag3\",\"tag4\"]}]}";
	private static final String JSON_WITHOUT_ID = "{\"images\":[{\"description\":\"asd\",\"rating\":5,\"tags\":[\"tag1\",\"tag2\"]},{\"id\":2,\"description\":\"fgh\",\"rating\":3,\"tags\":[\"tag3\",\"tag4\"]}]}";
	private static final String FILENAME1 = "filename1";
	private static final String FILENAME2 = "filename2";

	@Test
	public void update_with_two_images_in_db_expect_two_images_metadata_updated()
			throws IOException {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonData = mapper.readTree(JSON_VALID);

		Result result = callAction(
				controllers.routes.ref.ImageUpdateMultiple.update(),
				fakeRequest().withHeader("Content-Type", "application/json")
						.withJsonBody(jsonData));

		Image retrievedimage1 = imageQueries.getImage(image1.getId());
		Image retrievedimage2 = imageQueries.getImage(image2.getId());

		List<String> image1tags = new ArrayList<>();
		List<String> image2tags = new ArrayList<>();

		for (Tag tag : retrievedimage1.getTags()) {
			image1tags.add(tag.getName());
		}

		for (Tag tag : retrievedimage2.getTags()) {
			image2tags.add(tag.getName());
		}

		isOK(result);

		assertEquals(5, (int) retrievedimage1.getRating());
		assertEquals(3, (int) retrievedimage2.getRating());
		assertEquals("asd", retrievedimage1.getDescription());
		assertEquals("fgh", retrievedimage2.getDescription());

		assertThat(image1tags).contains("tag1", "tag2");
		assertThat(image2tags).contains("tag3", "tag4");
	}

	@Test
	public void update_with_two_images_in_db_and_json_missing_1_id_expect_one_images_metadata_updated()
			throws IOException {
		Image image1 = new Image();
		image1.setFilename(FILENAME1);

		Image image2 = new Image();
		image2.setFilename(FILENAME2);

		QueryImage imageQueries = new QueryImage(sessionFactory);
		imageQueries.addImage(image1);
		imageQueries.addImage(image2);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonData = mapper.readTree(JSON_WITHOUT_ID);

		Result result = callAction(
				controllers.routes.ref.ImageUpdateMultiple.update(),
				fakeRequest().withHeader("Content-Type", "application/json")
						.withJsonBody(jsonData));

		Image retrievedimage1 = imageQueries.getImage(1);
		Image retrievedimage2 = imageQueries.getImage(2);

		List<String> image2tags = new ArrayList<>();

		for (Tag tag : retrievedimage2.getTags()) {
			image2tags.add(tag.getName());
		}

		isOK(result);

		assertNull(retrievedimage1.getRating());
		assertEquals(3, (int) retrievedimage2.getRating());
		assertNull(retrievedimage1.getDescription());
		assertEquals("fgh", retrievedimage2.getDescription());

		assertThat(retrievedimage1.getTags()).isEmpty();
		assertThat(image2tags).contains("tag3", "tag4");
	}
}