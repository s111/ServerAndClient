package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.testServer;

import org.junit.Before;
import org.junit.Test;

import play.libs.F.Callback;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.TestBrowser;

public class ImageControllerTest {
	public static FakeApplication application;

	@Before
	public void startApp() {
		start(fakeApplication());
	}

	@Test
	public void getImageInfo_1_Id1() {
		Result result = callAction(controllers.routes.ref.ImageController
				.getImageInfo(1));

		isOK(result);
		isJSON(result);
		contains(result, "\"id\":1");
	}

	@Test
	public void getImages_0And25_Offset0AndLimit25() {
		Result result = callAction(controllers.routes.ref.ImageController
				.getImages(0, 25));

		isOK(result);
		isJSON(result);
		contains(result, "\"offset\":0,\"limit\":25");
	}
	
	@Test
	public void getImages_0And25_Next25() {
		Result result = callAction(controllers.routes.ref.ImageController
				.getImages(0, 25));

		isOK(result);
		isJSON(result);
		contains(result, "\"next\":\"http:///api/images?offset=25\"");
	}
	
	@Test
	public void getImages_0And25_Previous0() {
		Result result = callAction(controllers.routes.ref.ImageController
				.getImages(25, 25));

		isOK(result);
		isJSON(result);
		contains(result, "\"previous\":\"http:///api/images\"");
	}
	
	@Test
	public void getImages_0And25_First0() {
		Result result = callAction(controllers.routes.ref.ImageController
				.getImages(0, 25));

		isOK(result);
		isJSON(result);
		contains(result, "\"first\":\"http:///api/images\"");
	}
	
	@Test
	public void getImages_0And25_Last50() {
		Result result = callAction(controllers.routes.ref.ImageController
				.getImages(0, 25));

		isOK(result);
		isJSON(result);
		contains(result, "\"last\":\"http:///api/images?offset=50\"");
	}

	@Test
	public void getImage_1_PNG() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:9000/api/images/1/raw");
                assertThat(browser.pageSource()).contains("PNG");
            }
        });
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
