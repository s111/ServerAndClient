package controllers;

import static helpers.ResultHelper.contains;
import static helpers.ResultHelper.isJSON;
import static helpers.ResultHelper.isOK;
import static play.test.Helpers.callAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Image;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;
import queryDB.QueryImage;
import upload.Uploader;
import utils.HibernateUtil;

public class ImageControllerTest {
	List<Long> ids = new ArrayList<>();

	private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		sessionFactory = HibernateUtil.getNewSessionFactory();

		QueryImage queryImage = new QueryImage(sessionFactory);

		for (int i = 1; i < 10; i++) {
			Image image = new Image();
			image.setFilename(Uploader.IMAGE_DIRECTORY + "0" + i + ".png");

			queryImage.addImage(image);

			ids.add(image.getId());
		}

		Collections.sort(ids);
	}

	@After
	public void tearDown() {
		sessionFactory.close();
	}

	@Test
	public void getImages_with_offset_0_limit_4_expect_offset_0_limit_4() {
		Result result = callGetImages(0, 4);

		isOK(result);
		isJSON(result);
		contains(result, "\"offset\":0,\"limit\":4");
	}

	@Test
	public void getImages_with_offset_0_limit_4_expect_next_offset_4() {
		Result result = callGetImages(0, 4);

		isOK(result);
		isJSON(result);
		contains(result, "\"next\":\"http:///api/images?offset=4");
	}

	@Test
	public void getImages_with_offset_4_limit_4_expect_previous_offset_0() {
		Result result = callGetImages(4, 4);

		isOK(result);
		isJSON(result);
		contains(result, "\"previous\":\"http:///api/images?limit=4\"");
	}

	@Test
	public void getImages_with_offset_4_limit_4_expect_first_offset_0() {
		Result result = callGetImages(4, 4);

		isOK(result);
		isJSON(result);
		contains(result, "\"first\":\"http:///api/images?limit=4\"");
	}

	@Test
	public void getImages_with_offset_0_limit_4_expect_last_offset_8() {
		Result result = callGetImages(0, 4);

		isOK(result);
		isJSON(result);
		contains(result, "\"last\":\"http:///api/images?offset=8");
	}

	private Result callGetImages(int offset, int limit) {
		return callAction(controllers.routes.ref.ImageController.getImages(
				offset, limit));
	}
}
