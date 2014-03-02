package helpers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;
import play.mvc.Result;

public class ResultHelper {
	public static void contains(Result result, String string) {
		assertThat(contentAsString(result)).contains(string);
	}

	public static void isOK(Result result) {
		assertThat(status(result)).isEqualTo(OK);
	}

	public static void isJSON(Result result) {
		assertThat(contentType(result)).isEqualTo("application/json");
		assertThat(charset(result)).isEqualTo("utf-8");
	}
}
