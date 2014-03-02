package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.status;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;

public class ImageControllerTest {
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

	@Test
	public void getImageInfo_for_image_10_expect_last_68() {
		Result result = callGetImageInfo(10);

		isOK(result);
		isJSON(result);
		contains(result, "\"last\":\"http:///api/images/68\"");
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

	private Result callGetImageInfo(long id) {
		return callAction(controllers.routes.ref.GetImage.info(id));
	}

	private Result callGetImages(int offset, int limit) {
		return callAction(controllers.routes.ref.ImageController.getImages(
				offset, limit));
	}

	private void contains(Result result, String string) {
		assertThat(contentAsString(result)).contains(string);
	}

	private void isOK(Result result) {
		assertThat(status(result)).isEqualTo(OK);
	}

	private void isJSON(Result result) {
		assertThat(contentType(result)).isEqualTo("application/json");
		assertThat(charset(result)).isEqualTo("utf-8");
	}
}
