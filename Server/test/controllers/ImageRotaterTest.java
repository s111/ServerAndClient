package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.NOT_IMPLEMENTED;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

public class ImageRotaterTest {
	@Test
	public void rotate() {
		Result result = callAction(controllers.routes.ref.ImageRotater.rotate(
				1, 90));

		assertThat(status(result)).isEqualTo(NOT_IMPLEMENTED);
	}
}
