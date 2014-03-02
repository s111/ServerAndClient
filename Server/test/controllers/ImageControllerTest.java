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

public class ImageControllerTest {
	@Before
	public void startApp() {
		start(fakeApplication());
	}

	@Test
	public void getImages_with_offset_0_limit_25_expect_offset_0_limit_25() {
		Result result = callGetImages(0, 25);

		isOK(result);
		isJSON(result);
		contains(result, "\"offset\":0,\"limit\":25");
	}

	@Test
	public void getImages_with_offset_0_limit_25_expect_next_offset_25() {
		Result result = callGetImages(0, 25);

		isOK(result);
		isJSON(result);
		contains(result, "\"next\":\"http:///api/images?offset=25\"");
	}

	@Test
	public void getImages_with_offset_25_limit_25_expect__previous_offset_0() {
		Result result = callGetImages(25, 25);

		isOK(result);
		isJSON(result);
		contains(result, "\"previous\":\"http:///api/images\"");
	}

	@Test
	public void getImages_with_offset_0_limit_25_expect_first_offset_0() {
		Result result = callGetImages(0, 25);

		isOK(result);
		isJSON(result);
		contains(result, "\"first\":\"http:///api/images\"");
	}

	@Test
	public void getImages_with_offset_0_limit_25_expect_last_offset_50() {
		Result result = callGetImages(0, 25);

		isOK(result);
		isJSON(result);
		contains(result, "\"last\":\"http:///api/images?offset=50\"");
	}
	
	private Result callGetImages(int offset, int limit) {
		return callAction(controllers.routes.ref.ImageController.getImages(
				offset, limit));
	}
}
