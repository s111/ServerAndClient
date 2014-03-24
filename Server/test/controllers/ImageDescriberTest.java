package controllers;

import static org.junit.Assert.assertEquals;
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
import upload.Uploader;
import utils.HibernateUtil;

public class ImageDescriberTest {
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
	public void set_description_to_abc_expect_description_abc() {
		String filename = Uploader.IMAGE_DIRECTORY + "01.png";

		Image image = new Image();
		image.setFilename(filename);

		QueryImage queryImage = new QueryImage(sessionFactory);
		queryImage.addImage(image);

		long id = image.getId();

		Map<String, String> data = new HashMap<>();
		data.put("value", "abc");

		callAction(controllers.routes.ref.ImageDescriber.describe(id),
				new FakeRequest().withFormUrlEncodedBody(data));

		assertEquals("abc", queryImage.getImage(id).getDescription());
	}
}
