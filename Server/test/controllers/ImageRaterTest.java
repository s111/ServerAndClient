package controllers;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.HashMap;
import java.util.Map;

import models.ImageModel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeRequest;
import upload.Uploader;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

public class ImageRaterTest {
	@BeforeClass
	public static void startApp() {
		start(fakeApplication());
	}

	@AfterClass
	public static void stopApp() {
		stop(fakeApplication());
	}

	@Before
	public void resetDB() {
		String serverName = "default";

		EbeanServer server = Ebean.getServer(serverName);
		ServerConfig config = new ServerConfig();

		DdlGenerator ddl = new DdlGenerator();
		ddl.setup((SpiEbeanServer) server, new H2Platform(), config);
		ddl.runScript(false, ddl.generateDropDdl());
		ddl.runScript(false, ddl.generateCreateDdl());
	}

	@Test
	public void rate_image_with_5_expect_rating_5() {
		String filename = Uploader.IMAGE_DIRECTORY + "01.png";

		long id = ImageModel.create(filename).id;

		Map<String, String> data = new HashMap<>();
		data.put("value", "5");

		callAction(controllers.routes.ref.ImageRater.rate(id),
				new FakeRequest().withFormUrlEncodedBody(data));

		assertEquals(5, ImageModel.get(id).get().rating);
	}
}
