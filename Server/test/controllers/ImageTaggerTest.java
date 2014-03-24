package controllers;

import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.callAction;

import java.util.HashMap;
import java.util.Map;

import models.Image;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.FakeRequest;
import queryDB.QueryImage;
import queryDB.QueryTag;
import upload.Uploader;
import utils.HibernateUtil;

public class ImageTaggerTest {
	private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getNewSessionFactory();
	}

	@After
	public void tearDown() {
		sessionFactory.close();
	}

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
}
