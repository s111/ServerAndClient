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
import play.test.FakeApplication;

public class ImageControllerTest {
	public static FakeApplication application;

	@Before
	public void startApp() {
		start(fakeApplication());
	}

	@Test
	public void getImage_1_1() {
		Result result = callAction(controllers.routes.ref.ImageController
				.getImageInfo(1));

		isOK(result);
		isJSON(result);
		contains(result, "\"id\":1");
	}

	@Test
	public void getImages_0And25_0And25() {
		Result result = callAction(controllers.routes.ref.ImageController
				.getImages(0, 25));

		isOK(result);
		isJSON(result);
		contains(result, "\"offset\":0,\"limit\":25");
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
