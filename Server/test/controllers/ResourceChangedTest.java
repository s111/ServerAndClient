package controllers;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.header;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;
import models.ImageModel;
import models.TagModel;

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

public class ResourceChangedTest {
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
	public void infoChanged() {
		ImageModel imageModel = ImageModel.create("../../images/01.png");

		long id = imageModel.id;

		Result result;

		result = callAction(controllers.routes.ref.ResourceChanged.info(id));

		int changeCountBeforeModification = Integer.parseInt(header(
				"info-change-count", result));

		imageModel.setRating(5);
		imageModel.setDescription("description");
		imageModel.addTag(TagModel.create("tag"));

		result = callAction(controllers.routes.ref.ResourceChanged.info(id));

		int changeCountAfterModification = Integer.parseInt(header(
				"info-change-count", result));

		assertEquals(0, changeCountBeforeModification);
		assertEquals(3, changeCountAfterModification);
	}

	@Test
	public void getImages() {
		ImageModel.listChangeCount = 0;

		ImageModel.create("../../images/01.png");

		Result result;

		result = callAction(controllers.routes.ref.ResourceChanged.getImages(0,
				0));

		int sizeBeforeModification = Integer.parseInt(header(
				"list-change-count", result));

		ImageModel.create("../../images/02.png");

		result = callAction(controllers.routes.ref.ResourceChanged.getImages(0,
				0));

		int sizeAfterModification = Integer.parseInt(header(
				"list-change-count", result));

		assertEquals(1, sizeBeforeModification);
		assertEquals(2, sizeAfterModification);
	}
}
