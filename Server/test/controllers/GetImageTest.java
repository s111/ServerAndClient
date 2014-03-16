package controllers;

import static helpers.ResultHelper.contains;
import static helpers.ResultHelper.isJSON;
import static helpers.ResultHelper.isOK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.ImageModel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Result;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

public class GetImageTest {
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
		// Since I can't find a way to reset the database id auto increment, I'm
		// saving the ids
		ids = new ArrayList<>();

		for (int i = 1; i < 10; i++) {
			long id = ImageModel.create(ImageUploader.IMAGE_DIRECTORY + "0" + i
					+ ".png").id;

			ids.add(id);
		}

		Collections.sort(ids);
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
