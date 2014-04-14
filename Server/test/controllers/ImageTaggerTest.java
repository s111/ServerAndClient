package controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.callAction;
import helpers.WithDatabase;

import java.util.HashMap;
import java.util.Map;

import models.Image;
import models.Tag;

import org.junit.Test;

import play.test.FakeRequest;
import queryDB.QueryImage;
import queryDB.QueryTag;
import upload.Uploader;
import utils.HibernateUtil;

public class ImageTaggerTest extends WithDatabase {

	@Test
	public void tag_image_with_abc_expect_tag_name_abc() {
		String filename = Uploader.IMAGE_DIRECTORY + "01.png";

		Image image = new Image();
		image.setFilename(filename);

		QueryImage queryImage = new QueryImage(sessionFactory);
		queryImage.addImage(image);

		long id = image.getId();

		Map<String, String> data = new HashMap<>();
		data.put("value", "abc");

		callAction(controllers.routes.ref.ImageTagger.tag(id),
				new FakeRequest().withFormUrlEncodedBody(data));

		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		assertNotNull(queryTag.getTag("abc"));
	}

	@Test
	public void tag_image_with_abc_and_def_expect_tag_name_abc_and_def() {
		String filename = Uploader.IMAGE_DIRECTORY + "01.png";

		Image image = new Image();
		image.setFilename(filename);

		QueryImage queryImage = new QueryImage(sessionFactory);
		queryImage.addImage(image);

		long id = image.getId();

		Map<String, String> data = new HashMap<>();
		data.put("value", "abc,def");

		callAction(controllers.routes.ref.ImageTagger.tag(id),
				new FakeRequest().withFormUrlEncodedBody(data));

		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		assertNotNull(queryTag.getTag("abc"));
		assertNotNull(queryTag.getTag("def"));
	}

	@Test
	public void tag_image_with_abc_then_remove_tag_expect_no_tags() {
		String filename = Uploader.IMAGE_DIRECTORY + "01.png";
		String tagName = "abc";

		Tag tag = new Tag();
		tag.setName(tagName);

		Image image = new Image();
		image.setFilename(filename);

		QueryImage queryImage = new QueryImage(sessionFactory);
		queryImage.addImage(image);

		long id = image.getId();

		QueryTag queryTag = new QueryTag(sessionFactory);
		queryTag.tagImage(id, tagName);

		// Update image model with new tag list
		image = queryImage.getImage(id);

		assertTrue(image.getTags().contains(tag));

		callAction(controllers.routes.ref.ImageTagger.delete(id, tagName));

		// Update image model with new tag list
		image = queryImage.getImage(id);

		// There should be no tags now
		assertFalse(image.getTags().contains(tag));
	}
}
