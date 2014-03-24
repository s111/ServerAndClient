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

public class GetImageTest {
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
	public void getImageInfo_for_image_0_expect_id_0() {
		Result result = callGetImageInfo(ids.get(0));

		isOK(result);
		isJSON(result);
		contains(result, "\"id\":" + ids.get(0));
	}

	@Test
	public void getImageInfo_for_image_5_expect_next_image_6() {
		Result result = callGetImageInfo(ids.get(5));

		isOK(result);
		isJSON(result);
		contains(result, "\"next\":\"http:///api/images/" + ids.get(6) + "\"");
	}

	@Test
	public void getImageInfo_for_image_5_expect_previous_image_4() {
		Result result = callGetImageInfo(ids.get(5));

		isOK(result);
		isJSON(result);
		contains(result, "\"previous\":\"http:///api/images/" + ids.get(4)
				+ "\"");
	}

	@Test
	public void getImageInfo_for_image_5_expect_first_image_0() {
		Result result = callGetImageInfo(ids.get(5));

		isOK(result);
		isJSON(result);
		contains(result, "\"first\":\"http:///api/images/" + ids.get(0) + "\"");
	}

	@Test
	public void getImageInfo_for_image_5_expect_last_image_8() {
		Result result = callGetImageInfo(ids.get(5));

		isOK(result);
		isJSON(result);
		contains(result, "\"last\":\"http:///api/images/" + ids.get(8) + "\"");
	}

	private Result callGetImageInfo(long id) {
		return callAction(controllers.routes.ref.GetImage.info(id));
	}
}
