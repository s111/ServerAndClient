package controllers;

import static helpers.ResultHelper.contains;
import static helpers.ResultHelper.isJSON;
import static helpers.ResultHelper.isOK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;

public class GetImageTest {
	@Before
	public void startApp() {
		start(fakeApplication());
	}

	@Test
	public void getImageInfo_for_image_1_expect_id_1() {
		Result result = callGetImageInfo(1);

		isOK(result);
		isJSON(result);
		contains(result, "\"id\":1");
	}

	@Test
	public void getImageInfo_for_image_10_expect_next_image_11() {
		Result result = callGetImageInfo(10);

		isOK(result);
		isJSON(result);
		contains(result, "\"next\":\"http:///api/images/11\"");
	}

	@Test
	public void getImageInfo_for_image_10_expect_previous_image_9() {
		Result result = callGetImageInfo(10);

		isOK(result);
		isJSON(result);
		contains(result, "\"previous\":\"http:///api/images/9\"");
	}

	@Test
	public void getImageInfo_for_image_10_expect_first_image_1() {
		Result result = callGetImageInfo(10);

		isOK(result);
		isJSON(result);
		contains(result, "\"first\":\"http:///api/images/1\"");
	}
	
	private Result callGetImageInfo(long id) {
		return callAction(controllers.routes.ref.GetImage.info(id));
	}
}
