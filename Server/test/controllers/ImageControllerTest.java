package controllers;

import static helpers.ResultHelper.contains;
import static helpers.ResultHelper.isJSON;
import static helpers.ResultHelper.isOK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.ArrayList;
import java.util.List;

import models.ImageModel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

import play.mvc.Result;

public class ImageControllerTest {
	private List<Long> ids;
	
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
		
		addImagesToDB();
	}
	
	private void addImagesToDB() {
		// Since I can't find a way to reset the database id auto increment, I'm saving the ids
		ids = new ArrayList<>();
		
		for (int i = 1; i < 10; i++) {
			ids.add(ImageModel.create(ImageUploader.IMAGE_DIRECTORY + "0" + i + ".png").id);
		}
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
	public void getImages_with_offset_4_limit_4_expect__previous_offset_0() {
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
